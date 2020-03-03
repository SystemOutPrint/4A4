package org.nCoV2019

import org.nCoV2019.checker.ComposeGroupTypeCheckStrategy
import org.nCoV2019.checker.GroupTypeCheckStrategy
import org.nCoV2019.checker.special.PairSingleGroupTypeCheckStrategy
import org.nCoV2019.checker.special.SinglePairGroupTypeCheckStrategy
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.GroupType

object PokerChecker {

    private val specialGroupCheckStrategy: GroupTypeCheckStrategy = ComposeGroupTypeCheckStrategy(
            listOf(SinglePairGroupTypeCheckStrategy(), PairSingleGroupTypeCheckStrategy()))

    fun check(tk: String, cur: Group): Boolean {
        val ctx = GameContextHolder.getGameContext(tk) ?: throw IllegalStateException("no suck room, tk = $tk")
        val prev: Group = ctx.findHistoryEntry(1).group
        if (specialGroupCheckStrategy.check(ctx, cur, prev)) {
            return true
        }

        val curType = cur.type
        val prevType = prev.type
        if (curType == prevType && curType.sameTypeChecker.check(ctx, cur, prev)) {
            return true
        }

        if (checkGroupType(curType, prevType)) {
            return true
        }
        return false
    }

    private fun checkGroupType(curType: GroupType, prevType: GroupType): Boolean {
        if (prevType.prev.isEmpty()) {
            return false
        }

        return if (prevType.prev.contains(curType)) {
            true
        } else {
            prevType.prev.map { checkGroupType(curType, it) }.any { it }
        }
    }
}