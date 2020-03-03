package org.nCoV2019.protocol

import org.nCoV2019.GameContextHolder
import org.nCoV2019.PokerChecker
import org.nCoV2019.PokerClassifier
import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.Poker
import org.nCoV2019.protocol.Protocol.ProtocolType.CPokerAction
import org.nCoV2019.role.RoleManager
import org.nCoV2019.utils.SpecialRuleUtils
import org.nCoV2019.utils.SpecialRuleUtils.isSpecialAction
import org.slf4j.LoggerFactory

class CPokerAction(val tk: String,
                   val pokers: List<String>) : Protocol {

    override fun getType() = CPokerAction

    override fun process() {
        val ctx = GameContextHolder.getGameContext(tk)!!
        val roleId = RoleManager.getRoleId()
        if (!isSelfAction(ctx, roleId)) {
            logger.info("unexpected role = $roleId, expectRole = ${ctx.curActionPlayer.roleId}")
            return
        }

        val pokerList = pokers.map {
            val pair = it.split("_").map { it.toInt() }
            val realValue = ctx.getRealValueFromDisplayValue(pair[0])
            val type = Poker.Type.fromTypeCode(pair[1])
            Poker(realValue, type)
        }

        val isSyncGameState = if (ctx.specialPlayer != null) {
            processSpecial(roleId, ctx, pokerList)
        } else {
            processNormal(roleId, ctx, pokerList)
        }

        if (isSyncGameState) {
            ctx.isGameOver()
            ctx.syncAllPlayersGameState()
        }
    }

    private fun processSpecial(roleId: Long, ctx: GameContext, pokerList: List<Poker>): Boolean {
        val specialPlayer = ctx.specialPlayer!!
        if (specialPlayer.roleId != roleId) {
            return false
        }

        ctx.nextActionInterval = 30

        if (pokers.isEmpty()) {
            ctx.actionTimestamp = System.currentTimeMillis()
            ctx.specialPlayer = null
            return true
        } else {
            val groupType = PokerClassifier.classify(tk, pokerList)
            val group = Group(pokerList, groupType)
            val (prevRoleId, _) = ctx.findHistoryEntry(1)
            val isSpecialAction = isSpecialAction(ctx, group)
            if (prevRoleId == roleId || (isSpecialAction && PokerChecker.check(tk, group))) {
                ctx.prevNoAction = false
                ctx.addHistoryEntry(roleId, group)
                SpecialRuleUtils.checkSpecialAction(ctx, roleId, group)

                specialPlayer.pokers.removeIf { pokerList.contains(it) }

                ctx.curActionPlayer = specialPlayer.nextPlayer
                ctx.actionTimestamp = System.currentTimeMillis()

                ProtocolManager.reply(SPokerAction())
                return true
            }
        }
        ctx.specialPlayer = null
        return false
    }

    private fun processNormal(roleId: Long, ctx: GameContext, pokerList: List<Poker>): Boolean {
        ctx.nextActionInterval = 30
        if (pokers.isEmpty()) {
            ctx.prevNoAction = true
            ctx.curActionPlayer = ctx.curActionPlayer.nextPlayer
            ctx.actionTimestamp = System.currentTimeMillis()
            return true
        } else {
            val groupType = PokerClassifier.classify(tk, pokerList)
            val group = Group(pokerList, groupType)
            val (prevRoleId, _) = ctx.findHistoryEntry(1)
            if (prevRoleId == roleId || PokerChecker.check(tk, group)) {
                ctx.prevNoAction = false
                ctx.addHistoryEntry(roleId, group)
                SpecialRuleUtils.checkSpecialAction(ctx, roleId, group)

                val player = ctx.getPlayer(roleId)
                player.pokers.removeIf { pokerList.contains(it) }

                ctx.curActionPlayer = ctx.curActionPlayer.nextPlayer
                ctx.actionTimestamp = System.currentTimeMillis()

                ProtocolManager.reply(SPokerAction())
                return true
            }
        }
        return false
    }

    private fun isSelfAction(ctx: GameContext, roleId: Long): Boolean {
        val specialPlayer = ctx.specialPlayer
        if (specialPlayer != null && specialPlayer.roleId == roleId) {
            return true
        }

        return ctx.curActionPlayer.roleId == roleId
    }

    companion object {

        val logger = LoggerFactory.getLogger(CPokerAction::class.java)
    }
}