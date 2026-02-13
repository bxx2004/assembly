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
class ToolCallingRequest : AssemblyEntity{
    override fun id(): AssemblyIdentifier {
        return "assembly:tool_calling_request".id()
    }
    lateinit var meta: AssemblyPacketMeta
    var toolName: String? = null
    var methodName: String? = null
    var data = mapOf<String, Any?>()
}