package io.nuls.consensus.tx;

import io.nuls.consensus.constant.ConsensusConstant;
import io.nuls.consensus.entity.ConsensusAccount;
import io.nuls.core.utils.io.NulsByteBuffer;

/**
 * @author Niels
 * @date 2017/12/4
 */
public class ExitConsensusTransaction extends AbstractConsensusTransaction<ConsensusAccount> {
    public ExitConsensusTransaction() {
        super(ConsensusConstant.TX_TYPE_EXIT_CONSENSUS);
    }

    @Override
    protected ConsensusAccount parseBody(NulsByteBuffer byteBuffer) {
        ConsensusAccount ca = new ConsensusAccount();
        ca.parse(byteBuffer);
        return ca;
    }
}
