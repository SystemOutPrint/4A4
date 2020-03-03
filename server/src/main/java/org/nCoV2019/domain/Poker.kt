package org.nCoV2019.domain

import org.nCoV2019.domain.Poker.Type.Companion.fromTypeCode
import org.nCoV2019.domain.Poker.Type.KING

data class Poker(val value: Int, val type: Type) {

    enum class Type(val typeCode: Int) {
        HEART(0),
        SPADE(1),
        CLUB(2),
        DIAMOND(3),
        KING(4);

        companion object {

            private val map = values().associateBy { it.typeCode }

            fun fromTypeCode(typeCode: Int) = map[typeCode] ?: throw IllegalArgumentException("unknown typeCode: $typeCode")
        }
    }

    companion object {

        @JvmStatic
        fun newPoker(point: Int): Poker {
            // double king
            if (point >= 53) {
                return Poker(point % 53, KING)
            }

            // else
            val type = fromTypeCode(point%4)
            val value = (point - 1)/4 + 1
            return Poker(value, type)
        }
    }
}