package org.nCoV2019

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.nCoV2019.domain.GroupType
import org.nCoV2019.domain.Poker
import kotlin.test.assertEquals

class ClassifierTests {

    private val tk = "caijiahe"

    @Before
    fun before() {
        GameContextHolder.initGameContext(tk, 1)
    }

    @Test
    fun testSingleClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1))
        assertEquals(GroupType.SINGLE, type)
    }

    @Test
    fun testPairAndDoubleKingClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1, 2))
        assertEquals(GroupType.PAIR, type)

        val type1 = PokerClassifier.classify(tk, pokers(53, 54))
        assertEquals(GroupType.DOUBLE_KING, type1)
    }

    @Test
    fun testStraightClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1, 5, 9))
        assertEquals(GroupType.STRAIGHT, type)

        val type1 = PokerClassifier.classify(tk, pokers(44, 48, 52))
        assertEquals(GroupType.NONE, type1)
    }

    @Test
    fun testBombClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1, 2, 3))
        assertEquals(GroupType.BOMB, type)
    }

    @Test
    fun testDoubleStraightClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1, 2, 5, 6, 9, 10))
        assertEquals(GroupType.DOUBLE_STRAIGHT, type)

        val type1 = PokerClassifier.classify(tk, pokers(1, 2, 5, 6, 9, 10, 13, 14))
        assertEquals(GroupType.DOUBLE_STRAIGHT, type1)
    }

    @Test
    fun testBigBombClassifier() {
        val type = PokerClassifier.classify(tk, pokers(1, 2, 3, 4))
        assertEquals(GroupType.BIG_BOMB, type)
    }

    @Test
    fun test4A4Classifier() {
        val type = PokerClassifier.classify(tk, pokers(2, 3, 44))
        assertEquals(GroupType._4A4, type)

        GameContextHolder.initGameContext(tk, 1)
        val type1 = PokerClassifier.classify(tk, pokers(5, 6, 44))
        assertEquals(GroupType._4A4, type1)
    }

    @After
    fun after() {
        GameContextHolder.removeGameContext(tk)
    }

    private fun pokers(vararg points: Int) = points.map { Poker.newPoker(it) }.toList()
}