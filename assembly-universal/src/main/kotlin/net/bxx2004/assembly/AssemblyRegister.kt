package net.bxx2004.assembly

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.client.AssemblyInstance
import net.bxx2004.assembly.application.client.ClientInstanceManager
import net.bxx2004.assembly.network.controller.PacketReceiver

/**
 * @author 6hisea
 * @date  2025/10/19 12:02
 * @description: None
 */
class AssemblyRegister {

    fun listener(receiver: PacketReceiver) : AssemblyRegister {
        Assembly.addListener(receiver)
        return this
    }
    fun application(application:AssemblyApplication) : AssemblyRegister {
        Assembly.registerApplication(application)
        return this
    }
    fun clientFactory(clazz: Class<AssemblyInstance>) : AssemblyRegister {
        ClientInstanceManager.registerFactory(clazz)
        return this
    }
    fun build(func:AssemblyRegister.()->Unit) {
        func(this)
    }
}