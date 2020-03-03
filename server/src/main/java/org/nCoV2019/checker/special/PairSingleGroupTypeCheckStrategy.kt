package org.nCoV2019.checker.special

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.GroupType
import org.nCoV2019.domain.GroupType.PAIR
import org.nCoV2019.domain.GroupType.SINGLE
import org.nCoV2019.checker.SpecialGroupTypeCheckStrategy

class PairSingleGroupTypeCheckStrategy : SpecialGroupTypeCheckStrategy {

    override fun support(cur: GroupType, prev: GroupType) = cur == PAIR && prev == SINGLE

    override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
        val curPokers = cur.pokers
        val prevPokers = prev.pokers
        if (curPokers.first().value != prevPokers.first().value) {
            return false
        }
        return prevPokers.first().value == curPokers.first().value
    }
}