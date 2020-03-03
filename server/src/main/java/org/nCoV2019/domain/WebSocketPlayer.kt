package org.nCoV2019.domain

import org.nCoV2019.domain.GameContext.GameState.FINISH
import org.nCoV2019.protocol.ProtocolManager
import org.nCoV2019.protocol.SGameState

class WebSocketPlayer(roleId: Long, pokers: MutableList<Poker> = mutableListOf()) : Player(roleId, pokers) {

    override fun syncState(ctx: GameContext) {
        val myDisplayPokers = pokers.map { "${ctx.getDisplayValueFromRealValue(it.value)}_${it.type.typeCode}" }
        myDisplayPokers.sortedBy { it.split("_")[0].toInt() }

        val actionPlayerRoleId = if (ctx.specialPlayer != null) {
            ctx.specialPlayer!!.roleId
        } else  {
            ctx.curActionPlayer.roleId
        }

        val prevActionPokers = if (ctx.prevNoAction) {
            emptyList()
        } else {
            ctx.findHistoryEntry(1).group.pokers
                    .map { "${ctx.getDisplayValueFromRealValue(it.value)}_${it.type.typeCode}" }
        }

        val gameOverMsg = if (ctx.state == FINISH) {
            val player = ctx.getPlayer(roleId)
            if (player.pokers.isEmpty()) {
                "you win ^.^"
            } else {
                "game over =.="
            }
        } else ""

        val protocol = SGameState(prevPlayer.roleId,
                prevPlayer.pokers.size,
                nextPlayer.roleId,
                nextPlayer.pokers.size,
                prevActionPokers,
                roleId,
                myDisplayPokers,
                actionPlayerRoleId,
                30,
                gameOverMsg)
        ProtocolManager.reply(roleId, protocol)
    }

    override fun action(ctx: GameContext, rid: Long, pokerDisplayValues: List<Int>) {
        if (roleId == rid) {
            val pokerRealValues = pokerDisplayValues.map { ctx.getRealValueFromDisplayValue(it) }
            pokers = pokers.filter { !pokerRealValues.contains(it.value) }.toMutableList()
        }
    }
}