package org.nCoV2019.checker

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Group

interface SameGroupTypeCheckStrategy : GroupTypeCheckStrategy {

    companion object {

        val ALWAYS_TRUE = object : SameGroupTypeCheckStrategy {
            override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
                return true
            }
        }

        val ALWAYS_FALSE = object : SameGroupTypeCheckStrategy {
            override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
                return false
            }
        }


        val CHECK_FIRST = object : SameGroupTypeCheckStrategy {
            override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
                return cur.pokers.first().value > prev.pokers.first().value
            }
        }

        val CHECK_FIRST_AND_SIZE = object : SameGroupTypeCheckStrategy {
            override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
                val curPokers = cur.pokers
                val prevPoker = prev.pokers
                return curPokers.size == prevPoker.size && curPokers.first().value > prevPoker.first().value
            }
        }
    }
}