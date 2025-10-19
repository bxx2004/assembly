package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/18 12:50
 * @description: None
 */
class ResourceKey : AssemblyEntity{
    override fun id(): AssemblyIdentifier {
        return "application:resource_key".id()
    }
    lateinit var key: String
    lateinit var password: String
}