package net.bxx2004.assembly_bukkit

import io.netty.buffer.Unpooled
import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.server.ServerResourceManager
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.network.packet.AssemblyPacket.Companion.decode
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import net.bxx2004.assembly_bukkit.api.AssemblyPacketReceiveEvent
import net.bxx2004.assembly_bukkit.api.AssemblyRegisterEvent
import net.bxx2004.assembly_bukkit.api.PlayerConnectionEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.releaseResourceFile
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.util.bukkitPlugin
import java.io.File
@RuntimeDependencies(
    RuntimeDependency("org.apache.commons:commons-crypto:1.2.0"),
    RuntimeDependency("net.lingala.zip4j:zip4j:2.11.5"),
    RuntimeDependency("org.openjdk.nashorn:nashorn-core:15.4", relocate = ["!org.objectweb.asm", "!org.objectweb.asm9"]),
    RuntimeDependency("com.typesafe:config:1.4.5")
)
object AssemblyBukkit : Plugin() {
    @Config("options.yml", migrate = true, autoReload = true)
    lateinit var options: Configuration
        private set
    @Config("lang.yml", migrate = true, autoReload = true)
    lateinit var lang: Configuration
        private set
    override fun onEnable() {
        Assembly.init(Side.SERVER){ sender, packet ->
            AssemblyPacketReceiveEvent(packet,sender).call()
        }
        ServerResourceManager.registerKeyProvider(
            {
                options.getString("decrypt-key")!!
            },{
                options.getString("zip-password")!!
            }
        )
        Assembly.DATA_DIR = "plugins/assembly-bukkit"
        File(Assembly.DATA_DIR).mkdirs()

        releaseResourceFile("options.yml")
        releaseResourceFile("lang.yml")
        Bukkit.getMessenger().registerIncomingPluginChannel(bukkitPlugin, Assembly.CHANNEL){ channel, player, bytes->
            if (channel == Assembly.CHANNEL){
                val buf = Unpooled.wrappedBuffer(bytes)
                val obj = buf.array().decode()
                Assembly.callReceivePacket(player.asPacketSender,obj)
            }
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(bukkitPlugin, Assembly.CHANNEL)
        Assembly.register {
            AssemblyRegisterEvent(this).call()
        }

    }
    private val cache = arrayListOf<Player>()

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        PlayerConnectionEvent.Start(e.player).call()
        e.player.invokeMethod<Any?>("addChannel", Assembly.CHANNEL)
        ServerResourceManager.sendResourceKey(e.player.asPacketSender)
        PlayerConnectionEvent.Finish(e.player).call()
    }
}