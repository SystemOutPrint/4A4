package org.nCoV2019.checker.special

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.GroupType
import org.nCoV2019.domain.GroupType.PAIR
import org.nCoV2019.domain.GroupType.SINGLE
import org.nCoV2019.checker.SpecialGroupTypeCheckStrategy

class SinglePairGroupTypeCheckStrategy : SpecialGroupTypeCheckStrategy {

    override fun support(cur: GroupType, prev: GroupType) = cur == SINGLE && prev == PAIR

    override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
        val curPokers = cur.pokers
        val prevPokers = prev.pokers
        if (curPokers.first().value != prevPokers.first().value) {
            return false
        }

        return ctx.findHistoryEntry(2).let {
            val curValue = curPokers.first().value
            val prevPrevValue = it.group.pokers.first().value
            it.group.type == SINGLE && prevPrevValue == curValue && prevPokers.first().value == curValue
        }
    }
}