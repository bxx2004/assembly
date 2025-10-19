package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/19 11:33
 * @description: None
 */
class InstanceDeleteAll : AssemblyEntity {
    override fun id(): AssemblyIdentifier {
        return "application:instance_delete_all".id()
    }
    lateinit var appId:AssemblyIdentifier
}