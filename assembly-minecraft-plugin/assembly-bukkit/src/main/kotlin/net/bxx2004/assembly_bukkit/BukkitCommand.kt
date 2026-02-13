package net.bxx2004.assembly_bukkit

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.IShell
import net.bxx2004.assembly.application.server.ServerResourceManager
import net.bxx2004.assembly_bukkit.api.AssemblyCommandRegisterEvent
import net.bxx2004.assembly_bukkit.api.AssemblyPacketReceiveEvent
import net.bxx2004.assembly_bukkit.api.AssemblyPacketSendEvent
import net.bxx2004.assembly_bukkit.api.AssemblyResourceEncryptEvent
import net.bxx2004.assembly_bukkit.api.AssemblyResourcePackEvent
import net.bxx2004.script.source.source
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.command
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import java.io.File

/**
 * @author 6hisea
 * @date  2025/10/18 16:00
 * @description: None
 */
object BukkitCommand {
    private var listenerOut: ProxyListener? = null
    private var listenerIn: ProxyListener? = null

    @Awake(LifeCycle.ENABLE)
    private fun register() {
        command("assembly"){
            createHelper(true)
            literal("listen-out"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    if (listenerOut == null){
                        sender.sendMessage("listening...")
                        listenerOut = registerBukkitListener(AssemblyPacketSendEvent::class.java){
                            it.packet.debug()
                        }
                    }else{
                        sender.sendMessage("close...")
                        unregisterListener(listenerOut!!)
                        listenerOut = null
                    }
                }
            }
            literal("listen-in"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    if (listenerIn == null){
                        sender.sendMessage("listening...")
                        listenerIn = registerBukkitListener(AssemblyPacketReceiveEvent::class.java){
                            it.packet.debug()
                        }
                    }else{
                        sender.sendMessage("close...")
                        unregisterListener(listenerIn!!)
                        listenerIn = null
                    }
                }
            }
            literal("pack"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    AssemblyResourcePackEvent.Start().call()
                    ServerResourceManager.pack()
                    AssemblyResourcePackEvent.Finish(File(Assembly.DATA_DIR, "resource_pack.zip")).call()
                    sender.sendMessage("All resource is write to resource_pack.zip")
                }
            }
            literal("encrypt"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    AssemblyResourceEncryptEvent.Start().call()
                    ServerResourceManager.encrypt()
                    sender.sendMessage("All resource is encrypt to encrypt_resource")
                    AssemblyResourceEncryptEvent.Finish().call()
                }
            }
            literal("eval"){
                execute<CommandSender>{ sender, context, argument ->
                    sender.sendMessage("""
                            >>> ${argument.replace("eval ","")}
                            ${IShell.eval(source(argument.replace("eval ","")))}
                        """.trimIndent())
                }
            }
            literal("applications"){
                execute<ProxyCommandSender>{sender, context, argument ->
                    var info = "\n&b&l&oRunning &c&l&oApplications:\n"
                    Assembly.getApplications().forEach {
                        info += "  &b&l> &f&l${it.id}\n"
                    }
                    sender.sendMessage(info.colored())
                }
            }

            val e = AssemblyCommandRegisterEvent()
            e.call()
            if (e.getCommands().isNotEmpty()) {
                e.getCommands().forEach {
                    it(this)
                }
            }
        }
    }
}