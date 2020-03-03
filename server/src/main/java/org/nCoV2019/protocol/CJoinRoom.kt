package org.nCoV2019.protocol

import org.nCoV2019.GameContextHolder
import org.nCoV2019.domain.WebSocketPlayer
import org.nCoV2019.protocol.Protocol.ProtocolType.CJoinRoom
import org.nCoV2019.role.RoleManager
import org.nCoV2019.room.RoomManager

class CJoinRoom(val roomId: Long) : Protocol {

    override fun getType() = CJoinRoom

    override fun process() {
        val roleId = RoleManager.getRoleId()
        if (roleId > 0) {
            val tk = RoomManager.joinRoom(roomId)

            val protocol = SJoinRoom(tk)
            ProtocolManager.reply(protocol)

            val ctx = GameContextHolder.getGameContext(tk)
            val owner = WebSocketPlayer(roleId)
            ctx?.addPlayer(owner)
        }
    }
}