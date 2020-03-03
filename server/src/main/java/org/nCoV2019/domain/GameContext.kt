package org.nCoV2019.domain

import org.nCoV2019.GameContextHolder
import org.nCoV2019.PokerAllocator
import org.nCoV2019.domain.GameContext.GameState.FINISH
import org.nCoV2019.domain.GameContext.GameState.READY
import org.nCoV2019.domain.GameContext.GameState.STARTED
import org.nCoV2019.domain.GameContext.GameState.WAITING
import org.nCoV2019.domain.Group.Companion.EMPTY_GROUP
import org.nCoV2019.role.RoleManager
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class GameContext(private val history: MutableList<GameContextHistoryEntry>,
                  private val specialDisplayValue: Int,
                  val tk: String,
                  val roomId: Long,
                  @Volatile var state: GameState = WAITING,
                  var players: MutableList<Player> = mutableListOf(),
                  var actionTimestamp: Long = -1L,
                  var nextActionInterval: Int = 30,
                  private val maxPlayerNum: Int = 3) {

    private val eventLoop = Executors.newSingleThreadScheduledExecutor()

    lateinit var curActionPlayer: Player

    var prevNoAction = false

    var specialPlayer: Player? = null

    private var future: Future<*>? = null

    init {
        if (specialDisplayValue == 4 || specialDisplayValue < 3 || specialDisplayValue > 13) {
            throw IllegalArgumentException("invalid specialValue $specialDisplayValue")
        }
    }

    fun addPlayer(player: Player) {
        synchronized(this) {
            players.add(player)
            if (players.size == maxPlayerNum) {
                startGame()
            }
        }
    }

    fun getPlayer(roleId: Long) = players.first { it.roleId == roleId }

    private fun startGame() {
        state = READY

        var prevPlayer: Player? = null
        for (player in players) {
            if (prevPlayer != null) {
                prevPlayer.nextPlayer = player
                player.prevPlayer = prevPlayer
            }
            prevPlayer = player
        }
        prevPlayer!!.nextPlayer = players[0]
        players[0].prevPlayer = prevPlayer

        val roleIdPokersMap = PokerAllocator.allocate(players.map { it.roleId })
        players.forEach { player ->
            player.pokers = roleIdPokersMap[player.roleId]!!
        }

        val startActionPlayerIdx = ThreadLocalRandom.current().nextInt(0, players.size)
        this.curActionPlayer = players[startActionPlayerIdx]

        syncAllPlayersGameState()

        future = eventLoop.scheduleAtFixedRate(this::mainLoop, 10, 10, TimeUnit.SECONDS)

        actionTimestamp = System.currentTimeMillis()
        state = STARTED
    }

    private fun mainLoop() {
        if (state == STARTED) {
            checkAllPlayerState()

            val interval = if (specialPlayer == null) {
                30_000
            } else {
                10_000
            }

            if (System.currentTimeMillis() - actionTimestamp > interval) {
                // syn player no action
                prevNoAction = true
                curActionPlayer = curActionPlayer.nextPlayer
                actionTimestamp = System.currentTimeMillis()
                syncAllPlayersGameState()
            }
        }
    }

    private fun checkAllPlayerState() {
        val shouldCloseRoom = players.all {
            val channel = RoleManager.getChannelByRoleId(it.roleId)
            channel?.let { !channel.isOpen } ?: true
        }

        if (shouldCloseRoom) {
            GameContextHolder.removeGameContext(tk)
        }
    }

    fun close() {
        future?.cancel(true)
    }

    fun syncAllPlayersGameState() {
        players.forEach { it.syncState(this) }
    }

    fun findHistoryEntry(tailNum: Int) : GameContextHistoryEntry {
        if (history.size >= tailNum) {
            return history[history.size - tailNum]
        }
        return GameContextHistoryEntry(-1, EMPTY_GROUP)
    }

    fun addHistoryEntry(roleId: Long, group: Group) {
        history.add(GameContextHistoryEntry(roleId, group))
    }

    fun getSpecialDisplayValue() = specialDisplayValue

    fun getSpecialRealValue() = getRealValueFromDisplayValue(specialDisplayValue)

    fun getRealValueFromDisplayValue(displayValue: Int): Int {
        return when {
            displayValue == 14 -> 14
            displayValue == specialDisplayValue -> 13
            displayValue == 2 -> 12
            displayValue == 1 -> 11
            displayValue < specialDisplayValue -> displayValue - 2
            else -> displayValue - 3
        }
    }

    fun getDisplayValueFromRealValue(realValue: Int): Int {
        return when {
            realValue == 14 -> 14
            realValue == 13 -> specialDisplayValue
            realValue == 12 -> 2
            realValue == 11 -> 1
            realValue + 2 < specialDisplayValue -> realValue + 2
            else -> realValue + 3
        }
    }

    fun isGameOver() {
        for (player in players) {
            if (player.pokers.isEmpty()) {
                state = FINISH
                future?.cancel(true)
            }
        }
    }

    enum class GameState {
        WAITING,
        READY,
        STARTED,
        FINISH
    }
}