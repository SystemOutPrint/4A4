package org.nCoV2019.domain

abstract class Player(val roleId: Long,
                      var pokers: MutableList<Poker> = mutableListOf()) {

    lateinit var nextPlayer: Player

    lateinit var prevPlayer: Player

    abstract fun action(ctx: GameContext, rid: Long, pokerDisplayValues: List<Int>)

    abstract fun syncState(ctx: GameContext)
}