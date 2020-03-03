package org.nCoV2019.domain

import org.nCoV2019.domain.GroupType.NONE

data class Group(val pokers: List<Poker>, val type: GroupType) {

    companion object {

        val EMPTY_GROUP = Group(emptyList(), NONE)
    }
}