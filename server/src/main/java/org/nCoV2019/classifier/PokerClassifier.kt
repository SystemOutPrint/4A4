package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

interface PokerClassifier {

    fun classify(ctx: GameContext, pokers: List<Poker>): Boolean
}