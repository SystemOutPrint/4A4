package org.nCoV2019.domain

import org.nCoV2019.checker.GroupTypeCheckStrategy
import org.nCoV2019.checker.SameGroupTypeCheckStrategy.Companion.ALWAYS_FALSE
import org.nCoV2019.checker.SameGroupTypeCheckStrategy.Companion.ALWAYS_TRUE
import org.nCoV2019.checker.SameGroupTypeCheckStrategy.Companion.CHECK_FIRST
import org.nCoV2019.checker.SameGroupTypeCheckStrategy.Companion.CHECK_FIRST_AND_SIZE
import org.nCoV2019.classifier.DoubleKingPokerClassifier
import org.nCoV2019.classifier.NonePokerClassifier
import org.nCoV2019.classifier.Poker4A4Classifier
import org.nCoV2019.classifier.PokerClassifier
import org.nCoV2019.classifier.SamePokerClassifier
import org.nCoV2019.classifier.StraightPokerClassifier

enum class GroupType(val sameTypeChecker: GroupTypeCheckStrategy,
                     val classifier: PokerClassifier,
                     vararg val prev: GroupType) {

    _4A4(ALWAYS_FALSE, Poker4A4Classifier()),

    DOUBLE_KING(ALWAYS_FALSE, DoubleKingPokerClassifier(), _4A4),

    BIG_BOMB(CHECK_FIRST, SamePokerClassifier(4), DOUBLE_KING),

    DOUBLE_STRAIGHT(CHECK_FIRST_AND_SIZE, StraightPokerClassifier(2), BIG_BOMB),

    BOMB(CHECK_FIRST, SamePokerClassifier(3), BIG_BOMB),

    STRAIGHT(CHECK_FIRST_AND_SIZE, StraightPokerClassifier(1), BOMB, DOUBLE_STRAIGHT),

    PAIR(object : GroupTypeCheckStrategy {
        override fun check(ctx: GameContext, cur: Group, prev: Group): Boolean {
            return cur.pokers.first().value >= prev.pokers.first().value
        }
    }, SamePokerClassifier(2, true), BOMB),

    SINGLE(CHECK_FIRST, SamePokerClassifier(1), BOMB),

    NONE(ALWAYS_TRUE, NonePokerClassifier(), SINGLE, PAIR, STRAIGHT, BOMB, DOUBLE_STRAIGHT, BIG_BOMB, DOUBLE_KING, _4A4),
}