package net.bxx2004.assembly_bukkit

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.server.ServerResourceManager
import net.bxx2004.assembly_bukkit.api.AssemblyPacketReceiveEvent
import net.bxx2004.assembly_bukkit.api.AssemblyPacketSendEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.command
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import taboolib.expansion.createHelper
import taboolib.module.chat.colored

/**
 * @author 6hisea
 * @date  2025/10/18 16:00
 * @description: None
 */
object BukkitCommand {
    var listenerOut: ProxyListener? = null
    var listenerIn: ProxyListener? = null
    @Awake(LifeCycle.ENABLE)
    fun register() {
        command("assembly"){
            createHelper(true)
            literal("listen-out"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    if (listenerOut == null){
                        listenerOut = registerBukkitListener(AssemblyPacketSendEvent::class.java){
                            it.packet.debug()
                        }
                    }else{
                        unregisterListener(listenerOut!!)
                        listenerOut = null
                    }
                }
            }
            literal("listen-in"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    if (listenerIn == null){
                        listenerIn = registerBukkitListener(AssemblyPacketReceiveEvent::class.java){
                            it.packet.debug()
                        }
                    }else{
                        unregisterListener(listenerIn!!)
                        listenerIn = null
                    }
                }
            }
            literal("pack"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    ServerResourceManager.pack()
                    sender.sendMessage("All resource is write to resource_pack.zip")
                }
            }
            literal("encrypt"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    ServerResourceManager.encrypt()
                    sender.sendMessage("All resource is encrypt to encrypt_resource")
                }
            }
            literal("applications"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    var info = "\n&b&l&oRunning &c&l&oApplications:\n"
                    Assembly.getApplications().forEach {
                        info += "&b&l> &f&l${it.id}\n"
                    }
                    sender.sendMessage(info.colored())
                }
            }
        }
    }
}