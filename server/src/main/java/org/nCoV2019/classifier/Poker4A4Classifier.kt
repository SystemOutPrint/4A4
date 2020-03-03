package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

class Poker4A4Classifier : PokerClassifier {

    override fun classify(ctx: GameContext, pokers: List<Poker>): Boolean {
        val sortedPokers = pokers.map { ctx.getDisplayValueFromRealValue(it.value) }.sorted()
        return sortedPokers == listOf(1, 4, 4)
    }
}