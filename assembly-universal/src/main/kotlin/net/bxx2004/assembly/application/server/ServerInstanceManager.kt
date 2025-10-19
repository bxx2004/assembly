package net.bxx2004.assembly.application.server

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.application.client.AssemblyInstance
import net.bxx2004.assembly.application.entity.InstanceAdd
import net.bxx2004.assembly.application.entity.InstanceDelete
import net.bxx2004.assembly.application.entity.InstanceDeleteAll

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity

/**
 * @author 6hisea
 * @date  2025/10/18 16:45
 * @description: None
 */
object ServerInstanceManager{
    fun AssemblyApplication.addInstance(ins: AssemblyInstance,sender: PacketSender) {
        (instances as ArrayList<AssemblyInstance>).add(ins)
        AssemblyEntity.build<InstanceAdd> {
            appId = this@addInstance.id
            instanceId = ins.id
            attrs = ins.getAttrs()
        }.send(sender)
    }
    fun AssemblyApplication.removeInstance(ins: AssemblyInstance,sender: PacketSender) {
        (instances as ArrayList<AssemblyInstance>).remove(ins)
        AssemblyEntity.build<InstanceDelete> {
            appId = this@removeInstance.id
            instanceId = ins.id
        }.send(sender)
    }
    fun AssemblyApplication.removeInstanceById(id: AssemblyIdentifier,sender: PacketSender) {
        (instances as ArrayList<AssemblyInstance>).removeIf { it.id == id }
        AssemblyEntity.build<InstanceDelete> {
            appId = this@removeInstanceById.id
            instanceId = id
        }.send(sender)
    }
    fun AssemblyApplication.removeAllInstance(sender: PacketSender) {
        (instances as ArrayList<AssemblyInstance>).clear()
        AssemblyEntity.build<InstanceDeleteAll> {
            appId = this@removeAllInstance.id
        }.send(sender)
    }
}