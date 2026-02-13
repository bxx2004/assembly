package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2026/1/4 12:43
 * @description: None
 */
class ResourceShowMissing : AssemblyEntity{
    override fun id(): AssemblyIdentifier {
        return "application:resource_show_missing".id()
    }
}