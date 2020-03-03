package org.nCoV2019.room

import org.nCoV2019.GameContextHolder
import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.WebSocketPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

object RoomManager {

    private val roomIdCounter = AtomicLong(1)

    private val roomMap = ConcurrentHashMap<Long, RoomInfo>()

    fun createRoom(ownerId: Long): Pair<Long, String> {
        val roomId = roomIdCounter.getAndIncrement()
        val tk = UUID.randomUUID().toString()
        val ctx = GameContextHolder.initGameContext(tk, roomId)

        val owner = WebSocketPlayer(ownerId)
        ctx.addPlayer(owner)

        roomMap[roomId] = RoomInfo(ownerId, tk, ctx)
        return Pair(roomId, tk)
    }

    fun joinRoom(roomId: Long): String {
        val room = roomMap[roomId] ?: error("no such room: $roomId")
        return room.tk
    }

    fun removeRoom(roomId: Long) {
        roomMap.remove(roomId)
    }

    class RoomInfo(val ownerId: Long,
                   val tk: String,
                   val ctx: GameContext)
}