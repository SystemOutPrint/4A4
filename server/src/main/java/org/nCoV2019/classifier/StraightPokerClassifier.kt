package org.nCoV2019.classifier

import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.Poker

class StraightPokerClassifier(private val gap: Int) : PokerClassifier {

    override fun classify(ctx: GameContext, pokers: List<Poker>): Boolean {
        val size = pokers.size
        return size%gap == 0 && size >= 3*gap && pokers.let {
            var lastValue = -1
            for (i in 0 until it.size/gap) {
                val curValue = it[i*gap].value
                val nextValue = it[(i + 1)*gap - 1].value
                if (curValue == ctx.getSpecialRealValue()) {
                    return false
                } else if (curValue != nextValue) {
                    return false
                } else if (lastValue > 0 && curValue - lastValue != 1) {
                    return false
                }
                lastValue = curValue
            }
            return true
        }
    }
}