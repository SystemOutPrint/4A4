package org.nCoV2019.checker

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group

class ComposeGroupTypeCheckStrategy(private val strategys: List<GroupTypeCheckStrategy>): GroupTypeCheckStrategy {

    override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
        for (strategy in strategys) {
            if (strategy is SpecialGroupTypeCheckStrategy && !strategy.support(cur.type, prev.type)) {
                continue
            }
            if (strategy.check(ctx, cur, prev)) {
                return true
            }
        }
        return false
    }
}