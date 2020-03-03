package org.nCoV2019

import org.nCoV2019.domain.Poker

object PokerAllocator {

    fun allocate(roleIds: List<Long>): MutableMap<Long, MutableList<Poker>> {
        var allPokers: List<Int> = (1..54).map { it }.toMutableList()
        allPokers = allPokers.shuffled()

        val pokerNumPerRole = 54/roleIds.size
        val rolePokerMap = mutableMapOf<Long, List<Int>>()
        for (i in 0 until roleIds.size) {
            val pokers = allPokers.subList(i*pokerNumPerRole, (i + 1)*pokerNumPerRole)
            rolePokerMap[roleIds[i]] = pokers
        }
        return rolePokerMap.mapValues { (_, allPokerWithRole) ->
            val pokers = allPokerWithRole.map { Poker.newPoker(it) }.toMutableList()
            pokers.sortByDescending { it.value }
            pokers
        }.toMutableMap()
    }
}