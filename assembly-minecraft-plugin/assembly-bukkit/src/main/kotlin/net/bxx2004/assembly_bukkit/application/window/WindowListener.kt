package net.bxx2004.assembly_bukkit.application.window

import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstance
import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstanceFromLocal
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.application.server.ServerInstanceManager.addInstance
import net.bxx2004.assembly.application.server.ServerInstanceManager.sync
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import net.bxx2004.assembly_bukkit.api.AssemblyCommandRegisterEvent
import net.bxx2004.assembly_bukkit.api.AssemblyRegisterEvent
import net.bxx2004.assembly_bukkit.utils.addInstance
import net.bxx2004.assembly_minecraft.application.window.WindowApplication
import net.bxx2004.assembly_minecraft.application.window.instances.WindowIcon
import net.bxx2004.assembly_minecraft.application.window.instances.WindowTitle
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.player
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author 6hisea
 * @date  2026/2/11 10:44
 * @description: None
 */
object WindowListener {
    @SubscribeEvent
    fun reg(e: AssemblyRegisterEvent){
        e.register.application(WindowApplication)
            .registerServerInstanceFromLocal<WindowIcon>("window/icon")
            .registerServerInstanceFromLocal<WindowTitle>("window/title")
    }
    @SubscribeEvent
    fun command(e: AssemblyCommandRegisterEvent){
        e.register {
            literal("window"){
                player("player") {
                    literal("set"){
                        literal("icon"){
                            dynamic("id") {
                                suggestion<ProxyCommandSender> { _, _ ->
                                    WindowApplication.instances
                                        .filterIsInstance<WindowIcon>()
                                        .map { it.id.toString() }
                                }
                                execute<ProxyCommandSender> { sender, context,_->
                                    val target = context.player("player").origin as Player
                                    val id = context["id"].id()
                                    val result = target.addInstance(WindowApplication,id)
                                    sender.sendMessage(result.message)
                                }
                            }
                        }
                        literal("title"){
                            dynamic("id") {
                                suggestion<ProxyCommandSender> { _, _ ->
                                    WindowApplication.instances
                                        .filterIsInstance<WindowTitle>()
                                        .map { it.id.toString() }
                                }
                                execute<ProxyCommandSender> { sender, context,_->
                                    val target = context.player("player").origin as Player
                                    val id = context["id"].id()
                                    val result = target.addInstance(WindowApplication,id)
                                    sender.sendMessage(result.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}