package org.nCoV2019

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.nCoV2019.domain.Group
import org.nCoV2019.domain.GroupType
import org.nCoV2019.domain.Poker
import kotlin.test.assertEquals

class RuleTests {

    private val tk = "caijiahe"

    @Before
    fun before() {
        GameContextHolder.initGameContext(tk, 1)
    }

    @Test
    fun testSingleNone() {
        val result = PokerChecker.check(tk, group(GroupType.SINGLE, 1))
        assertEquals(true, result)
    }

    @Test
    fun testSingleSingle1() {
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.SINGLE, 5))
        assertEquals(true, result)
    }

    @Test
    fun testSingleSingle2() {
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(5, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.SINGLE, 1))
        assertEquals(false, result)
    }

    @Test
    fun testPairNone() {
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 1, 2))
        assertEquals(true, result)
    }

    @Test
    fun testPairPair1() {
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR, 1, 2))
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 5, 6))
        assertEquals(true, result)
    }

    @Test
    fun testPairPair2() {
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR, 5, 6))
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 1, 2))
        assertEquals(false, result)
    }

    @Test
    fun testPairPair3() {
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR, 3, 4))
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 1, 2))
        assertEquals(true, result)
    }

    @Test
    fun testSinglePair1() {
        // AA -> A
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 2, 3))
        assertEquals(true, result)
    }

    @Test
    fun testSinglePair2() {
        // 22 -> A
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.PAIR, 5, 6))
        assertEquals(false, result)
    }

    @Test
    fun testSinglePairSingle() {
        // A -> AA -> A
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(2, group(GroupType.PAIR, 2, 3))
        val result = PokerChecker.check(tk, group(GroupType.SINGLE, 4))
        assertEquals(true, result)
    }

    @Test
    fun testStraightNone() {
        val result = PokerChecker.check(tk, group(GroupType.STRAIGHT, 1, 5, 9))
        assertEquals(true, result)
    }

    @Test
    fun testStraightStraight1() {
        // 234 -> 123
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT, 1, 5, 9))
        val result = PokerChecker.check(tk, group(GroupType.STRAIGHT, 6, 10, 14))
        assertEquals(true, result)
    }

    @Test
    fun testStraightStraight2() {
        // 123 -> 234
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT, 6, 10, 14))
        val result = PokerChecker.check(tk, group(GroupType.STRAIGHT, 1, 5, 9))
        assertEquals(false, result)
    }

    @Test
    fun testStraightStraight3() {
        // 234 -> 1234
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT, 6, 10, 14))
        val result = PokerChecker.check(tk, group(GroupType.STRAIGHT, 1, 5, 9, 13))
        assertEquals(false, result)
    }

    @Test
    fun testBombNone() {
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 1, 2, 3))
        assertEquals(true, result)
    }

    @Test
    fun testBombBomb1() {
        // 222 -> 111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB, 1, 2, 3))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombBomb2() {
        // 111 -> 222
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB, 6, 10, 14))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 1, 5, 9))
        assertEquals(false, result)
    }

    @Test
    fun testBombSingle1() {
        // 222 -> 1
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombSingle2() {
        // 222 -> 3
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 9))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombPair1() {
        // 222 -> 1
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR, 1, 2))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombPair2() {
        // 222 -> 3
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR, 9, 10))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombStraight1() {
        // 222 -> 123
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT, 1, 6, 9))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBombStraight2() {
        // 222 -> 345
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT,  9, 13, 17))
        val result = PokerChecker.check(tk, group(GroupType.BOMB, 5, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleStraightSingle() {
        // 223344 -> 1
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 1))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 5, 6, 9, 10, 13, 14))
        assertEquals(false, result)
    }

    @Test
    fun testDoubleStraightPair() {
        // 223344 -> 11
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR,  1, 2))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 5, 6, 9, 10, 13, 14))
        assertEquals(false, result)
    }

    @Test
    fun testDoubleStraightBomb() {
        // 223344 -> 111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB,  1, 2, 3))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 5, 6, 9, 10, 13, 14))
        assertEquals(false, result)
    }

    @Test
    fun testDoubleStraightStraight() {
        // 223344 -> 123
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT,  1, 5, 9))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 5, 6, 9, 10, 13, 14))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleStraightDoubleStraight1() {
        // 223344 -> 112233
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  1, 2, 7, 8, 11, 12))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 5, 6, 9, 10, 13, 14))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleStraightDoubleStraight2() {
        // 112233 -> 223344
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  5, 6, 9, 10, 13, 14))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 1, 2, 7, 8, 11, 12))
        assertEquals(false, result)
    }

    @Test
    fun testDoubleStraightDoubleStraight3() {
        // 112233 -> 112233
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  3, 4, 5, 6, 9, 10))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_STRAIGHT, 1, 2, 7, 8, 11, 12))
        assertEquals(false, result)
    }

    @Test
    fun testBigBombSingle() {
        // 1111 -> 2
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 5))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombPair() {
        // 1111 -> 22
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR,  5, 6))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombBomb() {
        // 1111 -> 222
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB,  5, 6, 7))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombStraight() {
        // 1111 -> 234
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT,  5, 9, 13))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombDoubleStraight3() {
        // 1111 -> 223344
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  7, 8, 11, 12, 13, 14))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombBigBomb1() {
        // 2222 -> 1111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BIG_BOMB,  1, 2, 3, 4))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 5, 6, 7, 8))
        assertEquals(true, result)
    }

    @Test
    fun testBigBombBigBomb2() {
        // 1111 -> 2222
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BIG_BOMB,  5, 6, 7, 8))
        val result = PokerChecker.check(tk, group(GroupType.BIG_BOMB, 1, 2, 3, 4))
        assertEquals(false, result)
    }

    @Test
    fun testDoubleKingSingle() {
        // 1111 -> 2
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 5))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleKingPair() {
        // 1111 -> 22
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR,  5, 6))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleKingBomb() {
        // 1111 -> 222
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB,  5, 6, 7))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleKingStraight() {
        // 1111 -> 234
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT,  5, 9, 13))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleKingDoubleStraight() {
        // 1111 -> 223344
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  7, 8, 11, 12, 13, 14))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun testDoubleKingBigBomb() {
        // 2222 -> 1111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BIG_BOMB,  1, 2, 3, 4))
        val result = PokerChecker.check(tk, group(GroupType.DOUBLE_KING, 53, 54))
        assertEquals(true, result)
    }

    @Test
    fun test4a4Single() {
        // 1111 -> 2
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.SINGLE, 5))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4Pair() {
        // 1111 -> 22
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.PAIR,  5, 6))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4Bomb() {
        // 1111 -> 222
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BOMB,  5, 6, 7))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4Straight() {
        // 1111 -> 234
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.STRAIGHT,  5, 9, 13))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4DoubleStraight() {
        // 1111 -> 223344
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_STRAIGHT,  7, 8, 11, 12, 13, 14))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4BigBomb() {
        // 2222 -> 1111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.BIG_BOMB,  1, 2, 3, 4))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4DoubleKing() {
        // 2222 -> 1111
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType.DOUBLE_KING, 53, 54))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(true, result)
    }

    @Test
    fun test4a4and4a4() {
        // 4a4 -> 4a4
        GameContextHolder.getGameContext(tk)!!.addHistoryEntry(1, group(GroupType._4A4, 15, 16, 2))
        val result = PokerChecker.check(tk, group(GroupType._4A4, 13, 14, 1))
        assertEquals(false, result)
    }

    @After
    fun after() {
        GameContextHolder.removeGameContext(tk)
    }

    private fun group(type: GroupType, vararg points: Int): Group {
        val pokers = points.map { Poker.newPoker(it) }.toList()
        return Group(pokers, type)
    }
}