package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/18 12:35
 * @description: None
 */
class FunctionResponse<T> : AssemblyEntity {
    override fun id(): AssemblyIdentifier {
        return "application:function_response".id()
    }
    lateinit var meta: AssemblyPacketMeta
    var response: T?=null
}