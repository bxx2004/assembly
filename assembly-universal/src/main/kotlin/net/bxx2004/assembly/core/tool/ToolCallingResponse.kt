package net.bxx2004.assembly.core.tool

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2026/2/10 16:38
 * @description: None
 */
class ToolCallingResponse : AssemblyEntity{
    override fun id(): AssemblyIdentifier {
        return "assembly:tool_calling_response".id()
    }
    var result:Any? = null
    lateinit var meta: AssemblyPacketMeta
}