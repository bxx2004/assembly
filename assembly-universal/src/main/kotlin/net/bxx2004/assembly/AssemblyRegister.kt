package net.bxx2004.assembly

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.client.AssemblyInstance
import net.bxx2004.assembly.application.client.ClientInstanceManager
import net.bxx2004.assembly.application.server.ServerInstanceManager
import net.bxx2004.assembly.data.Side
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
    fun application(application:AssemblyApplication) : AssemblyApplication {
        Assembly.registerApplication(application)
        return application
    }
    companion object{
        fun AssemblyApplication.registerServerInstance(instance: AssemblyInstance):AssemblyApplication {
            if (Assembly.side == Side.SERVER){
                ServerInstanceManager.registerServerInstance(this, instance)
            }
            return this
        }
        fun AssemblyApplication.registerClientInstance(instance: Class<out AssemblyInstance>):AssemblyApplication {
            if (Assembly.side == Side.CLIENT){
                ClientInstanceManager.registerFactory(instance)
            }
            return this
        }
    }
}