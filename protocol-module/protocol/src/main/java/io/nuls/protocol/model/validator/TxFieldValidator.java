/**
 * MIT License
 * *
 * Copyright (c) 2017-2019 nuls.io
 * *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.protocol.model.validator;

import io.nuls.kernel.constant.TransactionErrorCode;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.validate.NulsDataValidator;
import io.nuls.kernel.validate.ValidateResult;

/**
 * @author Niels
 */
@Component
public class TxFieldValidator implements NulsDataValidator<Transaction> {

    public final static int MAX_TX_TYPE = 60000;


    @Override
    public ValidateResult validate(Transaction tx) {
        boolean result = true;
        do {
            if (tx == null) {
                result = false;
                break;
            }
            if (tx.getHash() == null || tx.getHash().size() == 0 || tx.getHash().size() > 70) {
                result = false;
                break;
            }
            if (tx.getType() == 0 || tx.getType() > MAX_TX_TYPE) {
                result = false;
                break;
            }
            if (tx.getTime() == 0) {
                result = false;
                break;
            }
        } while (false);
        if (!result) {
            return ValidateResult.getFailedResult(this.getClass().getName(), TransactionErrorCode.TX_DATA_VALIDATION_ERROR);
        }
        return ValidateResult.getSuccessResult();
    }
}
