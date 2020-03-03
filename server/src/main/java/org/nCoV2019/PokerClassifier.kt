package org.nCoV2019

import org.nCoV2019.domain.GroupType
import org.nCoV2019.domain.Poker

object PokerClassifier {

    fun classify(tk: String, pokers: List<Poker>): GroupType {
        val ctx = GameContextHolder.getGameContext(tk) ?: throw IllegalStateException("no suck room, tk = $tk")
        for (type in GroupType.values()) {
            if (type.classifier.classify(ctx, pokers)) {
                return type
            }
        }
        return GroupType.NONE
    }
}