package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/19 11:40
 * @description: None
 */
class InstanceAdd : AssemblyEntity {
    override fun id(): AssemblyIdentifier {
        return "application:instance_add".id()
    }
    lateinit var appId:AssemblyIdentifier
    lateinit var instanceId:AssemblyIdentifier
    var attrs = mapOf<String,Any?>()
    lateinit var name: String
}