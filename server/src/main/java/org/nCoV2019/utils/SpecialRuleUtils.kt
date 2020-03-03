package org.nCoV2019.utils

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.GroupType

object SpecialRuleUtils {

    fun checkSpecialAction(ctx: GameContext, roleId: Long, group: Group) {
        val prevPrevGroup = ctx.findHistoryEntry(2).group
        val prevGroup = ctx.findHistoryEntry(1).group
        if (prevPrevGroup.type == GroupType.SINGLE && prevGroup.type == GroupType.PAIR && group.type == GroupType.SINGLE) {
            val prevPrevValue = prevPrevGroup.pokers.first().value
            val prevValue = prevGroup.pokers.first().value
            val curValue = group.pokers.first().value
            if (prevPrevValue == prevValue && prevValue == curValue) {
                matchPlayerPokers(ctx, roleId, curValue) { it == 1 }
            }
        } else if (prevGroup.type == GroupType.PAIR && group.type == GroupType.PAIR) {
            val prevValue = prevGroup.pokers.first().value
            val curValue = group.pokers.first().value
            if (prevValue == curValue) {
                matchPlayerPokers(ctx, roleId, curValue) { it >= 2 }
            }
        }
    }

    fun isSpecialAction(ctx: GameContext, group: Group): Boolean {
        val prevPrevGroup = ctx.findHistoryEntry(2).group
        val prevGroup = ctx.findHistoryEntry(1).group
        if (prevPrevGroup.type == GroupType.SINGLE && prevGroup.type == GroupType.PAIR && group.type == GroupType.SINGLE) {
            val prevPrevValue = prevPrevGroup.pokers.first().value
            val prevValue = prevGroup.pokers.first().value
            val curValue = group.pokers.first().value
            if (prevPrevValue == prevValue && prevValue == curValue) {
                return true
            }
        } else if (prevGroup.type == GroupType.PAIR && group.type == GroupType.PAIR) {
            val prevValue = prevGroup.pokers.first().value
            val curValue = group.pokers.first().value
            if (prevValue == curValue) {
                return true
            }
        }
        return false
    }

    private fun matchPlayerPokers(ctx: GameContext, roleId: Long, value: Int, countIf: (Int) -> Boolean) {
        for (player in ctx.players) {
            if (player.roleId != roleId) {
                val count = player.pokers.count { it.value == value }
                if (countIf.invoke(count)) {
                    ctx.specialPlayer = player
                    ctx.nextActionInterval = 10
                    return
                }
            }
        }
    }
}