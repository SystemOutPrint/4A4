package org.nCoV2019.websocket

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.nCoV2019.protocol.ProtocolManager.dispatch
import org.nCoV2019.role.RoleManager
import org.slf4j.LoggerFactory

class WebSocketHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {

    val logger = LoggerFactory.getLogger(WebSocketHandler::class.java)

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        val channel = ctx.channel()
        logger.info(channel.remoteAddress().toString() + ": " + msg.text())
        dispatch(ctx.channel(), msg.text())
    }

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        logger.info("创建连接: " + ctx.channel().id().asLongText())
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        logger.info("用户下线: " + ctx.channel().id().asLongText())
        RoleManager.offline()
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.channel().close()
    }
}