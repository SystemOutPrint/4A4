package org.nCoV2019

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.netty.channel.Channel
import io.netty.channel.DefaultChannelProgressivePromise
import jdk.nashorn.internal.objects.NativeDate.toJSON
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.nCoV2019.protocol.CCreateRoom
import org.nCoV2019.protocol.CJoinRoom
import org.nCoV2019.protocol.CLogin
import org.nCoV2019.protocol.CPokerAction
import org.nCoV2019.protocol.Protocol
import org.nCoV2019.protocol.ProtocolManager
import org.nCoV2019.protocol.SGameState
import org.nCoV2019.protocol.SJoinRoom
import org.nCoV2019.role.RoleManager
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class GameTest {

    private val logger = LoggerFactory.getLogger(GameTest::class.java)

    private val mockChannel1 = mock<Channel>()

    private val mockChannel2 = mock<Channel>()

    private val mockChannel3 = mock<Channel>()

    private lateinit var tk: String

    private val map = ConcurrentHashMap<Channel?, LinkedBlockingQueue<SGameState>>()

    @Before
    fun ready() {
        // login
        ProtocolManager.channelCtx.set(mockChannel1)
        CLogin("123", "456").process()

        ProtocolManager.channelCtx.set(mockChannel2)
        CLogin("134", "456").process()

        ProtocolManager.channelCtx.set(mockChannel3)
        CLogin("145", "456").process()


        // create room
        CCreateRoom(123).process()

        // join room
        consume({
            ProtocolManager.channelCtx.set(mockChannel2)
            CJoinRoom(1).process()
        }) { _, protocol ->
            val joinRoom = protocol as SJoinRoom
            tk = joinRoom.tk
            true
        }

        ProtocolManager.channelProtocolInterceptor = { channel, protocol ->
            logger.info("protocol: {}", ObjectMapper().writeValueAsString(protocol))
            if (protocol is SGameState) {
                val queue = map.computeIfAbsent(channel) { LinkedBlockingQueue() }
                queue.offer(protocol)
            }
            true
        }

        ProtocolManager.channelCtx.set(mockChannel3)
        CJoinRoom(1).process()
    }

    @Test
    fun test() {
        val ctx = GameContextHolder.getGameContext(tk)!!

        val queue1 = map[mockChannel1]!!
        val initState1 = queue1.poll()

        val queue2 = map[mockChannel2]!!
        val initState2 = queue2.poll()

        val queue3 = map[mockChannel3]!!
        val initState3 = queue3.poll()

        val curActionRoleId = initState1.curActionRoleId
        val curActionPlayer = ctx.players.first { it.roleId == curActionRoleId }
        val poker = curActionPlayer.pokers[0]

        val channel = RoleManager.getChannelByRoleId(curActionRoleId)
        ProtocolManager.channelCtx.set(channel)

        CPokerAction(tk, listOf("${ctx.getDisplayValueFromRealValue(poker.value)}_${poker.type.typeCode}")).process()
        if (ctx.specialPlayer == null) {
            assertEquals(curActionPlayer.nextPlayer.roleId, ctx.curActionPlayer.roleId)
        }
    }

    private fun consume(protocolProducer: () -> Unit, protocolConsumer: (Channel?, Protocol) -> Boolean) {
        ProtocolManager.channelProtocolInterceptor = protocolConsumer
        try {
            protocolProducer.invoke()
        } finally {
            ProtocolManager.clearChannelProtocolInterceptor()
        }
    }
}