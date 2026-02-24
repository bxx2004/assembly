package net.bxx2004.assembly_bukkit

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstanceFromLocal
import net.bxx2004.assembly.IShell
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.InstanceLoader
import net.bxx2004.assembly.application.server.ServerInstanceManager
import net.bxx2004.assembly.application.server.ServerResourceManager
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly_bukkit.api.AssemblyCommandRegisterEvent
import net.bxx2004.assembly_bukkit.api.AssemblyPacketReceiveEvent
import net.bxx2004.assembly_bukkit.api.AssemblyPacketSendEvent
import net.bxx2004.assembly_bukkit.api.AssemblyResourceEncryptEvent
import net.bxx2004.assembly_bukkit.api.AssemblyResourcePackEvent
import net.bxx2004.assembly_bukkit.tlibm.commondhelper.description
import net.bxx2004.assembly_bukkit.tlibm.commondhelper.literalWithHelper
import net.bxx2004.assembly_bukkit.tlibm.commondhelper.newCommand
import net.bxx2004.assembly_bukkit.utils.addInstance
import net.bxx2004.assembly_bukkit.utils.syncAllInstance
import net.bxx2004.assembly_minecraft.application.window.WindowApplication
import net.bxx2004.assembly_minecraft.application.window.instances.WindowTitle
import net.bxx2004.script.source.source
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.command
import taboolib.common.platform.command.player
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.configuration.util.getStringColored
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
        newCommand("assembly", aliases = listOf("as","ab"), permission = "assembly.admin"){
            literalWithHelper("debug"){
                description(AssemblyBukkit.lang.getStringColored("debug-description")?:"调试指令")
                literalWithHelper("monitor"){
                    description(AssemblyBukkit.lang.getStringColored("debug-monitor-description")?:"监听网络包事件")
                    literal("input"){
                        description(AssemblyBukkit.lang.getStringColored("debug-monitor-input-description")?:"输入")
                        execute<ProxyCommandSender>{sender, context, argument ->
                            if (listenerIn == null){
                                sender.sendMessage(AssemblyBukkit.lang.getStringColored("debug-monitor-start")?:"开始监听")
                                listenerIn = registerBukkitListener(AssemblyPacketReceiveEvent::class.java){
                                    it.packet.debug()
                                }
                            }else{
                                sender.sendMessage(AssemblyBukkit.lang.getStringColored("debug-monitor-stop")?:"结束监听")
                                unregisterListener(listenerIn!!)
                                listenerIn = null
                            }
                        }
                    }
                    literal("output"){
                        description(AssemblyBukkit.lang.getStringColored("debug-monitor-output-description")?:"输出")
                        execute<ProxyCommandSender>{sender, context, argument ->
                            if (listenerOut == null){
                                sender.sendMessage(AssemblyBukkit.lang.getStringColored("debug-monitor-start")?:"开始监听")
                                listenerOut = registerBukkitListener(AssemblyPacketSendEvent::class.java){
                                    it.packet.debug()
                                }
                            }else{
                                sender.sendMessage(AssemblyBukkit.lang.getStringColored("debug-monitor-stop")?:"结束监听")
                                unregisterListener(listenerOut!!)
                                listenerOut = null
                            }
                        }
                    }
                }
            }
            literalWithHelper("resource"){
                description(AssemblyBukkit.lang.getStringColored("resource-description")?:"资源包指令")
                literal("pack"){
                    description(AssemblyBukkit.lang.getStringColored("resource-pack")?:"资源包指令")
                    execute<ProxyCommandSender>{sender, context, argument ->
                        sender.sendMessage(AssemblyBukkit.lang.getStringColored("resource-pack-start")?:"开始打包资源文件")
                        AssemblyResourcePackEvent.Start().call()
                        ServerResourceManager.pack()
                        AssemblyResourcePackEvent.Finish(File(Assembly.DATA_DIR, "resource_pack.zip")).call()
                        sender.sendMessage(AssemblyBukkit.lang.getStringColored("resource-pack-end")?:"所有资源文件全部打包完成")
                    }
                }
                literal("encrypt"){
                    execute<ProxyCommandSender>{sender, context, argument ->
                        sender.sendMessage(AssemblyBukkit.lang.getStringColored("resource-encrypt-start")?:"开始加密资源文件")
                        AssemblyResourceEncryptEvent.Start().call()
                        ServerResourceManager.encrypt()
                        sender.sendMessage(AssemblyBukkit.lang.getStringColored("resource-encrypt-end")?:"所有资源文件加密完成")
                        AssemblyResourceEncryptEvent.Finish().call()
                    }
                }
            }
            literal("shell"){
                description(AssemblyBukkit.lang.getStringColored("shell-description")?:"进入JavaScript脚本环境")
                execute<CommandSender>{ sender, context, argument ->
                    sender.sendMessage("""
                            >>> ${argument.replace("shell ","")}
                            ${IShell.eval(source(argument.replace("shell ","")))}
                        """.trimIndent())
                }
            }
            literalWithHelper("reload"){
                description(AssemblyBukkit.lang.getStringColored("reload-description")?:"重载指令")
                literalWithHelper("server"){
                    description(AssemblyBukkit.lang.getStringColored("reload-server-description")?:"服务端重载操作")

                    literal("instance"){
                        description(AssemblyBukkit.lang.getStringColored("reload-server-instance-description")?:"重载服务端实例")
                        dynamic("appId") {
                            suggestion<ProxyCommandSender> { _, _ ->
                                Assembly.getApplications()
                                    .map { it.id.toString() }
                            }

                            execute<ProxyCommandSender> { sender, context, argument ->
                                val appId = context["appId"].id()
                                Assembly.getApplications().find { it.id == appId }?.let {
                                    ServerInstanceManager.reloadServerInstance(it)
                                    sender.sendMessage(AssemblyBukkit.lang.getStringColored("reload-server-instance-success")?:"重载服务端实例成功")
                                }
                            }
                        }
                    }
                }

                literalWithHelper("client"){
                    description(AssemblyBukkit.lang.getStringColored("reload-client-description")?:"客户端重载操作")
                    literal("instance"){
                        description(AssemblyBukkit.lang.getStringColored("reload-client-instance-description")?:"重载客户端实例")
                        player {
                            execute<ProxyCommandSender> { sender, context, argument ->
                                val player = context.player("player").origin as Player
                                Assembly.getApplications().forEach {
                                    val res =player.syncAllInstance(
                                        it
                                    )
                                    sender.sendMessage(res.message)
                                }
                            }
                            dynamic("appId") {
                                suggestion<ProxyCommandSender> { _, _ ->
                                    Assembly.getApplications()
                                        .map { it.id.toString() }
                                }

                                execute<ProxyCommandSender> { sender, context, argument ->
                                    val appId = context["appId"].id()
                                    val player = context.player("player").origin as Player
                                    val res =player.syncAllInstance(
                                        Assembly.getApplications().find { it.id == appId }!!
                                    )
                                    sender.sendMessage(res.message)
                                }

                                dynamic("instanceId") {
                                    suggestion<ProxyCommandSender> { _, context ->
                                        val appId = context["appId"].id()
                                        Assembly.getApplications().find { it.id == appId }?.instances?.map {
                                            it.id.toString()
                                        }
                                    }
                                    execute<ProxyCommandSender> { sender, context, argument ->
                                        val appId = context["appId"].id()
                                        val instanceId = context["instanceId"].id()
                                        val player = context.player("player").origin as Player
                                        val res =player.addInstance(
                                            Assembly.getApplications().find { it.id == appId }!!,instanceId,reload = true
                                        )
                                        sender.sendMessage(res.message)
                                    }
                                }

                            }
                        }
                    }
                }
            }
            literal("applications"){
                description(AssemblyBukkit.lang.getStringColored("applications-description")?:"查询所有已经注册的应用")
                execute<ProxyCommandSender>{sender, context, argument ->
                    var info = "\n&b&l&oRunning &c&l&oApplications:\n"
                    Assembly.getApplications().forEach {
                        info += "  &b&l> &f&l${it.id}(${it.instances.size})\n"
                    }
                    sender.sendMessage(info.colored())
                }
            }
            literalWithHelper("application"){
                description(AssemblyBukkit.lang.getStringColored("application-description")?:"应用注册的命令")
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
}