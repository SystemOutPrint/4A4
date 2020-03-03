package org.nCoV2019.protocol

import org.nCoV2019.GameContextHolder
import org.nCoV2019.PokerAllocator
import org.nCoV2019.protocol.Protocol.ProtocolType.CCreateRoom
import org.nCoV2019.room.RoomManager

class CCreateRoom(val roleId: Long) : Protocol {

    override fun getType() = CCreateRoom

    override fun process() {
        val (roomId, tk) = RoomManager.createRoom(roleId)
        val protocol = SCreateRoom(roomId, tk)
        ProtocolManager.reply(protocol)

//        // TODO
//        val ctx = GameContextHolder.initGameContext("123")
//        val pokers = PokerAllocator.allocate(listOf(123, 134, 145))
//        CLogin.logger.info("pokers: {}", pokers)
//
//        val actionPokers = pokers[134]!!.map { "${ctx.getDisplayValueFromRealValue(it.value)}_${it.type.typeCode}" }
//        val myPokers = pokers[123]!!.map { "${ctx.getDisplayValueFromRealValue(it.value)}_${it.type.typeCode}" }
//        CLogin.logger.info("actionPokers: {}, myPokers: {}", actionPokers, myPokers)
//        ProtocolManager.reply(SGameState(145, 10, 134, 4,
//                actionPokers, 123, myPokers, 145, 30))
    }
}