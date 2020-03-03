package org.nCoV2019.role

import io.netty.channel.Channel
import org.nCoV2019.protocol.ProtocolManager
import java.util.concurrent.ConcurrentHashMap

object RoleManager {

    private val roleInfoMap = ConcurrentHashMap<Long, RoleInfo>()
    private val channelRoleIdMap = ConcurrentHashMap<Channel, Long>()

    fun online(userName: String, password: String, channel: Channel): Long {
        if (userName.startsWith("1")) {
            // TODO
            val roleId = userName.toLong()
            roleInfoMap[roleId] = RoleInfo(roleId, channel)
            channelRoleIdMap[channel] = roleId
            return roleId
        }
        return -1
    }

    fun offline() {
        val roleId = getRoleId()
        roleInfoMap.remove(roleId)

        val channel = ProtocolManager.getChannel()
        channelRoleIdMap.remove(channel)
    }

    fun getRoleId(): Long {
        val channel = ProtocolManager.getChannel() ?: return -1
        return channelRoleIdMap[channel] ?: -1
    }

    fun getChannelByRoleId(roleId: Long) = roleInfoMap[roleId]?.channel

    data class RoleInfo(val roleId: Long, val channel: Channel)
}