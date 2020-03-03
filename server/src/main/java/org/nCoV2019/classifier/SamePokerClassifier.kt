package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

open class SamePokerClassifier(private val num: Int,
                               private val excludeKing: Boolean = false) : PokerClassifier {

    override fun classify(ctx: GameContext, pokers: List<Poker>): Boolean {
        return pokers.size == num && sameAll(pokers)
    }

    fun sameAll(pokers: List<Poker>): Boolean {
        val total = pokers.sumBy { it.value }
        if (excludeKing) {
            for (poker in pokers) {
                if (poker.type == Poker.Type.KING) {
                    return false
                }
            }
        }
        return total/pokers.size == pokers.map { it.value }.first()
    }
}