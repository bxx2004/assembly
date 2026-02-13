package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/29 10:52
 * @description: None
 */
class InstanceNow : AssemblyEntity {
    override fun id(): AssemblyIdentifier {
        return "application:instance_now".id()
    }
    lateinit var appId:AssemblyIdentifier
    lateinit var instanceId:AssemblyIdentifier
    var attrs = mapOf<String,Any?>()
    lateinit var name: String
}