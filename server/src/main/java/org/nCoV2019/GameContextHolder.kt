package org.nCoV2019

import org.nCoV2019.domain.GameContext
import org.nCoV2019.room.RoomManager

object GameContextHolder {

    private val tkCtxMap = mutableMapOf<String, GameContext>()

    fun getGameContext(tk: String) = tkCtxMap[tk]

    fun initGameContext(tk: String, roomId: Long): GameContext {
        val oldCtx = tkCtxMap[tk]

        var specialValue = oldCtx?.getSpecialDisplayValue()?.plus(1) ?: 3
        specialValue = when (specialValue) {
            4 -> 5
            13 -> 3
            else -> specialValue
        }

        val newCtx = GameContext(mutableListOf(), specialValue, tk, roomId)
        tkCtxMap[tk] = newCtx
        return newCtx
    }

    fun removeGameContext(tk: String) {
        tkCtxMap.remove(tk)?.let {
            it.close()
            RoomManager.removeRoom(it.roomId)
        }

    }
}