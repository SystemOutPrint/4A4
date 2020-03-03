package org.nCoV2019

import org.junit.Test
import org.nCoV2019.domain.GameContext
import org.nCoV2019.domain.GameContextHistoryEntry
import kotlin.test.assertEquals

class GameContextTests {

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidSpecialValue() {
        GameContext(emptyList<GameContextHistoryEntry>().toMutableList(), 4, "", 1)
    }

    @Test
    fun testGetRealValueFromDisplayValueWithSpecialValueIs3() {
        val ctx = GameContext(emptyList<GameContextHistoryEntry>().toMutableList(), 3, "", 1)
        assertEquals(1, ctx.getRealValueFromDisplayValue(4))
        assertEquals(7, ctx.getRealValueFromDisplayValue(10))
        assertEquals(11, ctx.getRealValueFromDisplayValue(1))
        assertEquals(12, ctx.getRealValueFromDisplayValue(2))
        assertEquals(13, ctx.getRealValueFromDisplayValue(3))
        assertEquals(14, ctx.getRealValueFromDisplayValue(14))
    }

    @Test
    fun testGetRealValueFromDisplayValueWithSpecialValueIs5() {
        val ctx = GameContext(emptyList<GameContextHistoryEntry>().toMutableList(), 5, "", 1)
        assertEquals(1, ctx.getRealValueFromDisplayValue(3))
        assertEquals(2, ctx.getRealValueFromDisplayValue(4))
        assertEquals(7, ctx.getRealValueFromDisplayValue(10))
        assertEquals(11, ctx.getRealValueFromDisplayValue(1))
        assertEquals(12, ctx.getRealValueFromDisplayValue(2))
        assertEquals(13, ctx.getRealValueFromDisplayValue(5))
        assertEquals(14, ctx.getRealValueFromDisplayValue(14))
    }

    @Test
    fun testGetDisplayValueFromRealValueWithSpecialValueIs3() {
        val ctx = GameContext(emptyList<GameContextHistoryEntry>().toMutableList(), 3, "", 1)
        assertEquals(4, ctx.getDisplayValueFromRealValue(1))
        assertEquals(10, ctx.getDisplayValueFromRealValue(7))
        assertEquals(1, ctx.getDisplayValueFromRealValue(11))
        assertEquals(2, ctx.getDisplayValueFromRealValue(12))
        assertEquals(3, ctx.getDisplayValueFromRealValue(13))
        assertEquals(14, ctx.getDisplayValueFromRealValue(14))
    }

    @Test
    fun testGetDisplayValueFromRealValueWithSpecialValueIs5() {
        val ctx = GameContext(emptyList<GameContextHistoryEntry>().toMutableList(), 5, "", 1)
        assertEquals(3, ctx.getDisplayValueFromRealValue(1))
        assertEquals(4, ctx.getDisplayValueFromRealValue(2))
        assertEquals(6, ctx.getDisplayValueFromRealValue(3))
        assertEquals(10, ctx.getDisplayValueFromRealValue(7))
        assertEquals(1, ctx.getDisplayValueFromRealValue(11))
        assertEquals(2, ctx.getDisplayValueFromRealValue(12))
        assertEquals(5, ctx.getDisplayValueFromRealValue(13))
        assertEquals(14, ctx.getDisplayValueFromRealValue(14))
    }
}