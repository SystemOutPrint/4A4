package org.nCoV2019.checker

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group

interface GroupTypeCheckStrategy {

    fun check(ctx: GameContext, cur: Group, prev: Group): Boolean
}