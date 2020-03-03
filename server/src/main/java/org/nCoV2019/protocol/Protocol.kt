package org.nCoV2019.protocol

import com.fasterxml.jackson.annotation.JsonIgnore

interface Protocol {

    @JsonIgnore
    fun getTypeCode(): Int = getType().ordinal

    @JsonIgnore
    fun getType(): ProtocolType

    fun process()

    enum class ProtocolType {
        CLogin,
        SLogin,
        CCreateRoom,
        SCreateRoom,
        CJoinRoom,
        SJoinRoom,
        CPokerAction,
        SGameState,
        SPokerAction;

        companion object {

            private val map = values().associateBy { it.ordinal }

            fun fromValue(code: Int): Class<Protocol> {
                val type = map[code] ?: error("no such type: $code")
                val className = "org.nCoV2019.protocol.${type.name}"
                return Class.forName(className) as Class<Protocol>
            }
        }
    }
}