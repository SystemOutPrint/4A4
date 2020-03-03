package org.nCoV2019.protocol

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.netty.channel.Channel
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.nCoV2019.protocol.Protocol.ProtocolType.Companion.fromValue
import org.nCoV2019.role.RoleManager
import org.slf4j.LoggerFactory

object ProtocolManager {

    private val logger = LoggerFactory.getLogger(ProtocolManager::class.java)
    
    val channelCtx = ThreadLocal<Channel>()

    var channelProtocolInterceptor: (Channel?, Protocol) -> Boolean = { _, _ -> false }

    fun dispatch(channel: Channel, msg: String) {
        try {
            channelCtx.set(channel)
            val line = msg.split(";")
            val code = line[0].toInt()
            if (code > 1 && RoleManager.getRoleId() <= 0) {
                logger.info("close not verify channel.")
                channel.close()
            }

            // deserialize
            val clazz = fromValue(code)
            val protocol = ObjectMapper().registerKotlinModule().readValue(line[1], clazz)
            protocol.process()
        } catch (t : Throwable) {
            logger.info("dispatch failed, ${t.message}")
        } finally {
            channelCtx.remove()
        }
    }

    fun reply(protocol: Protocol) {
        try {
            val channel = channelCtx.get()
            reply(channel, protocol)
        } catch (t : Throwable) {
            logger.info("replay failed, $t")
        }
    }

    fun reply(roleId: Long, protocol: Protocol) {
        try {
            val channel = RoleManager.getChannelByRoleId(roleId)
            reply(channel, protocol)
        } catch (t : Throwable) {
            logger.info("replay failed, $t")
        }
    }

    private fun reply(channel: Channel?, protocol: Protocol) {
        if (channelProtocolInterceptor.invoke(channel, protocol)) {
            return
        }

        try {
            val json = ObjectMapper().registerKotlinModule().writeValueAsString(protocol)
            val msg = "${protocol.getTypeCode()};$json"
            channel?.writeAndFlush(TextWebSocketFrame(msg))
        } catch (t: Throwable) {
            logger.info("replay failed, $t")
        }
    }

    fun clearChannelProtocolInterceptor() {
        channelProtocolInterceptor = { _, _ -> false }
    }

    fun getChannel() = channelCtx.get()
}