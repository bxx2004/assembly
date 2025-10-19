package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/18 12:02
 * @description: None
 */
class FunctionInvoke : AssemblyEntity{
    override fun id(): AssemblyIdentifier {
        return "application:function_invoke".id()
    }
    lateinit var meta: AssemblyPacketMeta
    lateinit var name: String
    var args: Array<out Any>? = null
}