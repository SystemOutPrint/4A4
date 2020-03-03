package org.nCoV2019.protocol

import org.nCoV2019.role.RoleManager
import org.slf4j.LoggerFactory

class CLogin(private val userName: String,
             private val password: String) : Protocol {

    override fun process() {
        val channel = ProtocolManager.getChannel()
        val roleId = RoleManager.online(userName, password, channel)
        if (roleId > 0) {
            val protocol = SLogin(roleId)
            ProtocolManager.reply(protocol)
        } else {
            channel.close()
        }
    }

    override fun getType() = Protocol.ProtocolType.CLogin

    companion object {

        val logger = LoggerFactory.getLogger(CLogin::class.java)
    }
}