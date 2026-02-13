package net.bxx2004.assembly

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.client.ClientInstanceManager
import net.bxx2004.assembly.application.handler.CustomRequestHandler
import net.bxx2004.assembly.application.resource.InstanceLoader
import net.bxx2004.assembly.application.server.ServerInstanceManager
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author 6hisea
 * @date  2025/10/19 12:02
 * @description: None
 */
class AssemblyRegister {
    val customRequestHandler = CopyOnWriteArrayList<CustomRequestHandler>()
    val tools = CopyOnWriteArrayList<Tool>()

    fun listener(receiver: PacketReceiver) : AssemblyRegister {
        Assembly.addListener(receiver)
        return this
    }

    fun tool(tool: Tool){
        tools.add(tool)
    }

    fun customPacketHandler(cqh:CustomRequestHandler) : AssemblyRegister{
        customRequestHandler.add(cqh)
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

        inline fun <reified T: AssemblyInstance>AssemblyApplication.registerServerInstanceFromLocal(dir: String):AssemblyApplication {
            if (Assembly.side == Side.SERVER){
                InstanceLoader.load<T>(dir,this)
            }
            return this
        }
        inline fun <reified T: AssemblyInstance>AssemblyApplication.reloadServerInstanceFromLocal(dir: String,senders: List<PacketSender>):AssemblyApplication {
            if (Assembly.side == Side.SERVER){
                InstanceLoader.reload<T>(dir,this,senders)
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