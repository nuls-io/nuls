/*
 * MIT License
 *
 * Copyright (c) 2017-2019 nuls.io
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

package io.nuls.protocol.rpc.resources;

import io.nuls.core.tools.log.Log;
import io.nuls.core.tools.param.AssertUtil;
import io.nuls.core.tools.str.StringUtils;
import io.nuls.kernel.constant.KernelErrorCode;
import io.nuls.kernel.context.NulsContext;
import io.nuls.kernel.exception.NulsException;
import io.nuls.kernel.lite.annotation.Autowired;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.*;
import io.nuls.kernel.utils.AddressTool;
import io.nuls.ledger.service.LedgerService;
import io.nuls.protocol.constant.ProtocolErroeCode;
import io.nuls.protocol.rpc.model.*;
import io.nuls.protocol.service.BlockService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

/**
 * @author: Niels Wang
 */
@Path("/block")
@Api(value = "/block", description = "Block")
@Component
public class BlockResource {

    @Autowired
    private BlockService blockService;

    @Autowired
    private LedgerService ledgerService;

    @GET
    @Path("/header/height/{height}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "根据区块高度查询区块头", notes = "result.data: blockHeaderJson 返回对应的区块头信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getHeaderByHeight(@ApiParam(name = "height", value = "区块高度", required = true)
                                             @PathParam("height") Integer height) {
        AssertUtil.canNotEmpty(height);
        Result<Block> blockResult = blockService.getBlock(height);
        if (blockResult.isFailed()) {
            return blockResult.toRpcClientResult();
        }
        BlockHeaderDto dto = null;
        try {
            dto = new BlockHeaderDto(blockResult.getData());
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(KernelErrorCode.IO_ERROR).toRpcClientResult();
        }
        return Result.getSuccess().setData(dto).toRpcClientResult();
    }


    @GET
    @Path("/header/txHash/height/{height}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "根据区块高度查询区块头", notes = "result.data: blockHeaderJson 返回对应的区块头信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getHeaderHashesByHeight(@ApiParam(name = "height", value = "区块高度", required = true)
                                                   @PathParam("height") Integer height) {
        AssertUtil.canNotEmpty(height);
        Result<Block> blockResult = blockService.getBlock(height);
        if (blockResult.isFailed()) {
            return blockResult.toRpcClientResult();
        }
        BlockHeaderDto dto = null;
        try {
            dto = new BlockHeaderDto(blockResult.getData());
            List<String> list = blockService.getBlockTxHash(height);
            dto.setTxHash(list);
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(KernelErrorCode.IO_ERROR).toRpcClientResult();
        }
        return Result.getSuccess().setData(dto).toRpcClientResult();
    }

    @GET
    @Path("/header/hash/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "根据区块hash查询区块头", notes = "result.data: blockHeaderJson 返回对应的区块头信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getHeader(@ApiParam(name = "hash", value = "区块hash", required = true)
                                     @PathParam("hash") String hash) {
        AssertUtil.canNotEmpty(hash);
        hash = StringUtils.formatStringPara(hash);
        if (!NulsDigestData.validHash(hash)) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }
        Result result = Result.getSuccess();
        Block block = null;
        try {
            block = blockService.getBlock(NulsDigestData.fromDigestHex(hash)).getData();
        } catch (NulsException e) {
            Log.error(e);
        }
        if (block == null) {
            return Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL).toRpcClientResult();
        }
        try {
            result.setData(new BlockHeaderDto(block));
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(KernelErrorCode.IO_ERROR).toRpcClientResult();
        }
        return result.toRpcClientResult();
    }

    @GET
    @Path("/header/txHash/hash/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "根据区块hash查询区块头", notes = "result.data: blockHeaderJson 返回对应的区块头信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getHeaderHash(@ApiParam(name = "hash", value = "区块hash", required = true)
                                         @PathParam("hash") String hash) {
        AssertUtil.canNotEmpty(hash);
        hash = StringUtils.formatStringPara(hash);
        if (!NulsDigestData.validHash(hash)) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }
        Result result = Result.getSuccess();
        Block block = null;
        try {
            block = blockService.getBlock(NulsDigestData.fromDigestHex(hash)).getData();
        } catch (NulsException e) {
            Log.error(e);
        }
        if (block == null) {
            return Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL).toRpcClientResult();
        }
        try {
            BlockHeaderDto dto = new BlockHeaderDto(block);
            List<String> list = blockService.getBlockTxHash(block.getHeader().getHeight());
            dto.setTxHash(list);
            result.setData(dto);
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(KernelErrorCode.IO_ERROR).toRpcClientResult();
        }
        return result.toRpcClientResult();
    }

    @GET
    @Path("/hash/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("根据区块hash查询区块，包含区块打包的所有交易信息，此接口返回数据量较多，谨慎调用")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult loadBlock(@ApiParam(name = "hash", value = "区块hash", required = true)
                                     @PathParam("hash") String hash) throws IOException {
        AssertUtil.canNotEmpty(hash);
        Result result;
        if (!NulsDigestData.validHash(hash)) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }
        Block block = null;
        try {
            // 包含智能合约内部转账(从合约转出)交易的区块
            block = blockService.getBlock(NulsDigestData.fromDigestHex(hash), true).getData();
        } catch (NulsException e) {
            Log.error(e);
        }
        if (block == null) {
            result = Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL);
        } else {
            result = Result.getSuccess();
            BlockDto dto = new BlockDto(block);
            fillBlockTxInputAddress(dto);
            calTransactionValue(dto);
            result.setData(dto);

        }
        return result.toRpcClientResult();
    }

    @GET
    @Path("/height/{height}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("根据区块高度查询区块，包含区块打包的所有交易信息，此接口返回数据量较多，谨慎调用")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getBlock(@ApiParam(name = "height", value = "区块高度", required = true)
                                    @PathParam("height") Long height) throws IOException {
        AssertUtil.canNotEmpty(height);
        Result result = Result.getSuccess();
        if (height < 0) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }

        // 包含智能合约内部转账(从合约转出)交易的区块
        Block block = blockService.getBlock(height, true).getData();
        if (block == null) {
            result = Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL);
        } else {
            BlockDto dto = new BlockDto(block);
            fillBlockTxInputAddress(dto);
            calTransactionValue(dto);
            result.setData(dto);
        }
        return result.toRpcClientResult();
    }

    @GET
    @Path("/newest")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询最新区块头信息", notes = "result.data: blockHeaderJson 返回对应的区块头信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getBestBlockHeader() throws IOException {
        Result result = Result.getSuccess();
        result.setData(new BlockHeaderDto(NulsContext.getInstance().getBestBlock()));
        return result.toRpcClientResult();
    }

    @GET
    @Path("/newest/height")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询最新区块高度")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = RpcClientResult.class)
    })
    public RpcClientResult getBestBlockHight() throws IOException {
        long hight = NulsContext.getInstance().getBestBlock().getHeader().getHeight();
        Map<String, Long> map = new HashMap<>();
        map.put("value", hight);
        return Result.getSuccess().setData(map).toRpcClientResult();
    }

    @GET
    @Path("/newest/hash")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询最新区块的hash")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = RpcClientResult.class)
    })
    public RpcClientResult getBestBlockHash() throws IOException {
        String hash = NulsContext.getInstance().getBestBlock().getHeader().getHash().getDigestHex();
        Map<String, String> map = new HashMap<>();
        map.put("value", hash);
        return Result.getSuccess().setData(map).toRpcClientResult();
    }

    @GET
    @Path("/bytes")
    @Produces(MediaType.APPLICATION_JSON)
    public RpcClientResult getBlockBytes(@QueryParam("hash") String hash) throws IOException {
        Result result;
        if (!NulsDigestData.validHash(hash)) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }
        Block block = null;
        try {
            // 包含智能合约内部转账(从合约转出)交易的区块
            block = blockService.getBlock(NulsDigestData.fromDigestHex(hash), true).getData();
        } catch (NulsException e) {
            Log.error(e);
        }
        if (block == null) {
            result = Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL);
        } else {
            result = Result.getSuccess();
            Map<String, String> map = new HashMap<>();
            map.put("value", Base64.getEncoder().encodeToString(block.serialize()));
            result.setData(map);
        }
        return result.toRpcClientResult();
    }

    @GET
    @Path("/bytes/height")
    @Produces(MediaType.APPLICATION_JSON)
    public RpcClientResult getBlockBytes(@QueryParam("height") long height) throws IOException {
        Result result;
        if (height < 0) {
            return Result.getFailed(KernelErrorCode.PARAMETER_ERROR).toRpcClientResult();
        }
        Block block = null;
        try {
            // 包含智能合约内部转账(从合约转出)交易的区块
            block = blockService.getBlock(height, true).getData();
        } catch (Exception e) {
            Log.error(e);
        }
        if (block == null) {
            result = Result.getFailed(ProtocolErroeCode.BLOCK_IS_NULL);
        } else {
            result = Result.getSuccess();
            Map<String, String> map = new HashMap<>();
            map.put("value", Base64.getEncoder().encodeToString(block.serialize()));
            result.setData(map);
        }
        return result.toRpcClientResult();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("根据区块高度查询区块列表，包含区块打包的所有交易信息，此接口返回数据量较多，谨慎调用")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockDto.class)
    })
    public RpcClientResult getBlockList(@QueryParam("startHeight") Long startHeight, @QueryParam("size") Long size) throws IOException {
        if (size > 100) {
            return RpcClientResult.getFailed("the size is too big");
        }
        long bestHeight = NulsContext.getInstance().getBestHeight();
        if (startHeight > bestHeight) {
            return RpcClientResult.getFailed("The start height is to high!");
        }
        List<BlockDto> list = new ArrayList<>();

        for (int i = 0; i < size && (startHeight + i) <= bestHeight; i++) {
            Block block = blockService.getBlock(startHeight + i).getData();
            if (null == block) {
                break;
            }
            BlockDto dto = new BlockDto(block);
            fillBlockTxInputAddress(dto);
            calTransactionValue(dto);
            list.add(dto);
        }
        Map<String, List<BlockDto>> map = new HashMap<>();
        map.put("list", list);
        return Result.getSuccess().setData(map).toRpcClientResult();

    }

    private void fillBlockTxInputAddress(BlockDto dto) {
        for (TransactionDto transaction : dto.getTxList()) {
            if (transaction.getInputs() == null || transaction.getInputs().isEmpty()) {
                continue;
            }
            for (InputDto inputDto : transaction.getInputs()) {
                Transaction tx;
                try {
                    tx = ledgerService.getTx(NulsDigestData.fromDigestHex(inputDto.getFromHash()));
                } catch (NulsException e) {
                    Log.error(e);
                    continue;
                }
                Coin coin = tx.getCoinData().getTo().get(inputDto.getFromIndex());
                //inputDto.setAddress(AddressTool.getStringAddressByBytes(coin.()));
                inputDto.setAddress(AddressTool.getStringAddressByBytes(coin.getAddress()));
            }
        }

    }

    /**
     * 计算交易实际发生的金额(通用)
     * Calculate the actual amount of the transaction.
     */
    private void calTransactionValue(BlockDto dto) {
        for (TransactionDto txDto : dto.getTxList()) {
            if (txDto == null) {
                break;
            }
            Set<String> inputAdressSet = null;
            if (txDto.getInputs() != null && !txDto.getInputs().isEmpty()) {
                List<InputDto> inputDtoList = txDto.getInputs();
                inputAdressSet = new HashSet<>(inputDtoList.size());
                for (InputDto inputDto : inputDtoList) {
                    inputAdressSet.add(inputDto.getAddress());
                }
            }
            Na value = Na.ZERO;
            List<OutputDto> outputDtoList = txDto.getOutputs();
            for (OutputDto outputDto : outputDtoList) {
                if (null != inputAdressSet && inputAdressSet.contains(outputDto.getAddress())) {
                    continue;
                }
                value = value.add(Na.valueOf(outputDto.getValue()));
            }
            txDto.setValue(value.getValue());
        }
    }


    @GET()
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("查询最近五块区块的基本信息（用于开发调试）")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = BlockInfoDto.class)
    })
    public RpcClientResult getBlockInfoList() throws IOException {
        Block bestBlock = NulsContext.getInstance().getBestBlock();
        List<BlockInfoDto> list = new ArrayList<>();
        int count = 0;
        NulsDigestData preHash = null;
        while (count < 5) {
            count++;
            BlockHeader header = null;
            if (null != preHash) {
                header = blockService.getBlockHeader(preHash).getData();
            } else {
                header = bestBlock.getHeader();
            }
            if (null == header) {
                return RpcClientResult.getFailed(KernelErrorCode.DATA_ERROR);
            }
            BlockInfoDto dto = new BlockInfoDto();
            dto.setHeight(header.getHeight());
            dto.setHash(header.getHash().getDigestHex());
            dto.setPackingAddress(AddressTool.getStringAddressByBytes(header.getPackingAddress()));
            dto.setTxCount(header.getTxCount());
            list.add(dto);
            preHash = header.getPreHash();
        }
        Map<String, List<BlockInfoDto>> map = new HashMap<>();
        map.put("list", list);
        return Result.getSuccess().setData(map).toRpcClientResult();

    }
}
