package org.nCoV2019.protocol

import org.nCoV2019.protocol.Protocol.ProtocolType.SLogin

class SLogin(val roleId: Long) : Protocol {

    override fun getType() = SLogin

    override fun process() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}