package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

class DoubleKingPokerClassifier : PokerClassifier {

    override fun classify(ctx: GameContext, pokers: List<Poker>): Boolean {
        return pokers.size == 2 && pokers.all { it.type == Poker.Type.KING }
    }
}