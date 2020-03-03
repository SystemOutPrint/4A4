package org.nCoV2019.protocol

import org.nCoV2019.protocol.Protocol.ProtocolType.SGameState

class SGameState(val prevRoleId: Long,
                 val prevPokerNums: Int,
                 val nextRoleId: Long,
                 val nextPokerNums: Int,
                 val actionPokers: List<String>,
                 val myRoleId: Long,
                 val myPokers: List<String>,
                 val curActionRoleId: Long,
                 val leftSecond: Int,
                 val gameOverMsg: String) : Protocol {

    override fun getType() = SGameState

    override fun process() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}