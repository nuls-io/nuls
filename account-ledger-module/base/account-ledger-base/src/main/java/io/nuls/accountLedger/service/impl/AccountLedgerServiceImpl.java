/*
 * MIT License
 *
 * Copyright (c) 2017-2018 nuls.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.nuls.accountLedger.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import io.nuls.account.model.Account;
import io.nuls.account.model.Address;
import io.nuls.account.model.Balance;
import io.nuls.account.service.AccountService;
import io.nuls.accountLedger.constant.AccountLedgerErrorCode;

import io.nuls.accountLedger.service.Balance.BalanceService;
import io.nuls.accountLedger.storage.po.TransactionInfoPo;
import io.nuls.accountLedger.storage.service.AccountLedgerStorageService;
import io.nuls.accountLedger.util.CoinComparator;
import io.nuls.core.tools.crypto.Base58;
import io.nuls.core.tools.log.Log;
import io.nuls.kernel.constant.ErrorCode;
import io.nuls.kernel.constant.NulsConstant;
import io.nuls.kernel.exception.NulsException;
import io.nuls.kernel.lite.annotation.Autowired;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.*;

import io.nuls.accountLedger.service.AccountLedgerService;
import io.nuls.kernel.utils.AddressTool;
import io.nuls.protocol.model.tx.TransferTransaction;

import java.util.*;

/**
 * @author Facjas
 * @date 2018/5/10.
 */
@Component
public class AccountLedgerServiceImpl implements AccountLedgerService {

    @Autowired
    private AccountLedgerStorageService storageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BalanceService balanceService;

    private static List<Account> localAccountList;

    @Override
    public void init() {
        reloadAccount();
    }

    @Override
    public Result<Integer> save(Transaction tx) {
        if (!isLocalTransaction(tx)) {
            return Result.getFailed().setData(new Integer(0));
        }

        TransactionInfoPo txInfoPo = new TransactionInfoPo(tx);
        List<byte[]> addresses = new ArrayList<>();
        byte[] addressesBytes = tx.getAddress();

        if (addressesBytes == null || addressesBytes.length == 0) {
            return Result.getSuccess().setData(new Integer(0));
        }

        if (addressesBytes.length % Address.size() != 0) {
            return Result.getFailed(AccountLedgerErrorCode.PARAMETER_ERROR);
        }

        for (int i = 0; i < addressesBytes.length / Address.size(); i++) {
            byte[] tmpAddress = new byte[Address.size()];
            System.arraycopy(addressesBytes, i * Address.size(), tmpAddress, 0, Address.size());
            if (isLocalAccount(tmpAddress)) {
                addresses.add(tmpAddress);
            }
        }

        Result result = storageService.saveLocalTxInfo(txInfoPo, addresses);

        if (result.isFailed()) {
            return result;
        }
        result = storageService.saveLocalTx(tx);
        if (result.isFailed()) {
            storageService.deleteLocalTxInfo(txInfoPo);
        }
        return result;
    }

    @Override
    public Result<Integer> saveList(List<Transaction> txs) {
        List<Transaction> txListToSave = getLocalTransaction(txs);
        List<Transaction> savedTxList = new ArrayList<>();
        Result result;
        for (int i = 0; i < txListToSave.size(); i++) {
            result = save(txListToSave.get(i));
            if (result.isSuccess()) {
                savedTxList.add(txListToSave.get(i));
            } else {
                rollback(savedTxList, false);
                return result;
            }
        }
        return Result.getSuccess().setData(txListToSave.size());
    }

    @Override
    public Result<Integer> rollback(Transaction tx) {
        if (!isLocalTransaction(tx)) {
            return Result.getFailed().setData(new Integer(0));
        }

        TransactionInfoPo txInfoPo = new TransactionInfoPo(tx);
        Result result = storageService.deleteLocalTxInfo(txInfoPo);

        if (result.isFailed()) {
            return result;
        }
        result = storageService.deleteLocalTx(tx);

        return result;
    }

    @Override
    public Result<Integer> rollback(List<Transaction> txs) {
        return rollback(txs, true);
    }

    public Result<Integer> rollback(List<Transaction> txs, boolean isCheckMine) {
        List<Transaction> txListToRollback;
        if (isCheckMine) {
            txListToRollback = getLocalTransaction(txs);
        } else {
            txListToRollback = txs;
        }
        for (int i = 0; i < txListToRollback.size(); i++) {
            rollback(txListToRollback.get(i));
        }

        return Result.getSuccess().setData(new Integer(txListToRollback.size()));
    }

    @Override
    public Result<Balance> getBalance(byte[] address) throws NulsException {
        if (address == null || address.length != AddressTool.HASH_LENGTH) {
            return Result.getFailed(AccountLedgerErrorCode.PARAMETER_ERROR);
        }

        if (!isLocalAccount(address)) {
            return Result.getFailed(AccountLedgerErrorCode.ACCOUNT_NOT_EXIST);
        }

        Balance balance = balanceService.getBalance(Base58.encode(address));

        if (balance == null) {
            return Result.getFailed(AccountLedgerErrorCode.ACCOUNT_NOT_EXIST);
        }

        return Result.getSuccess().setData(balance);
    }

    @Override
    public List<Coin> getCoinData(byte[] address, Na amount, int size) throws NulsException {
        List<Coin> coinList = storageService.getCoinBytes(address);
        if (coinList.isEmpty()) {
            return coinList;
        }
        Collections.sort(coinList, CoinComparator.getInstance());

        boolean enough = false;
        List<Coin> coins = new ArrayList<>();
        Na values = Na.ZERO;
        for (int i = 0; i < coinList.size(); i++) {
            Coin coin = coinList.get(i);
            coins.add(coin);
            size += coin.size();
            values = values.add(coin.getNa());
            if (values.isGreaterOrEquals(values)) {
                enough = true;
                break;
            }
        }
        if (!enough) {
            coins = new ArrayList<>();
        }
        return coins;
    }

    @Override
    public boolean isLocalAccount(byte[] address) {
        if (localAccountList == null || localAccountList.size() == 0) {
            return false;
        }

        for (int i = 0; i < localAccountList.size(); i++) {
            if (Arrays.equals(localAccountList.get(i).getAddress().getBase58Bytes(), address)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Account> getLocalAccountList() {
        return localAccountList;
    }

    @Override
    public Result transfer(byte[] from, byte[] to, Na values) {
        try {
            List<Coin> coinList = getCoinData(from, values);
            if(coinList.isEmpty()) {
                return Result.getFailed("balance not enough");
            }
            CoinData coinData = new CoinData();
            coinData.setFrom(coinList);

            TransferTransaction tx = new TransferTransaction();

        } catch (NulsException e) {
            Log.error(e);
            return Result.getFailed(e.getErrorCode());
        }
    }


    protected List<Transaction> getLocalTransaction(List<Transaction> txs) {
        List<Transaction> resultTxs = new ArrayList<>();
        if (txs == null || txs.size() == 0) {
            return resultTxs;
        }
        if (localAccountList == null || localAccountList.size() == 0) {
            return resultTxs;
        }
        Transaction tmpTx;
        for (int i = 0; i < txs.size(); i++) {
            tmpTx = txs.get(i);
            if (isLocalTransaction(tmpTx)) {
                resultTxs.add(tmpTx);
            }
        }
        return resultTxs;
    }

    protected boolean isLocalTransaction(Transaction tx) {
        if (tx == null) {
            return false;
        }
        if (localAccountList == null || localAccountList.size() == 0) {
            return false;
        }
        List<byte[]> addresses = tx.getAllRelativeAddress();
        for (int j = 0; j < addresses.size(); j++) {
            if (isLocalAccount(addresses.get(j))) {
                return true;
            }
        }
        return false;
    }

    public void reloadAccount() {
        localAccountList = accountService.getAccountList().getData();
    }

}
