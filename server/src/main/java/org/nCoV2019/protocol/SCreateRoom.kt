package org.nCoV2019.protocol

import org.nCoV2019.protocol.Protocol.ProtocolType.SCreateRoom

class SCreateRoom(val roomId: Long,
                  val tk: String) : Protocol {

    override fun getType() = SCreateRoom

    override fun process() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}