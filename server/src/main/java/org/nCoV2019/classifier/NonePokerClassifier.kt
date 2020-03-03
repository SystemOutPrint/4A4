package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

class NonePokerClassifier : PokerClassifier {

    override fun classify(ctx: GameContext, pokers: List<Poker>): Boolean {
        return false
    }
}