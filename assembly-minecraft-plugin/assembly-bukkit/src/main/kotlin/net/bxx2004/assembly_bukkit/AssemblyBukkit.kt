package net.bxx2004.assembly_bukkit

import io.netty.buffer.Unpooled
import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.packet.AssemblyPacket.Companion.decode
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import net.bxx2004.assembly_bukkit.api.AssemblyPacketReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.platform.util.bukkitPlugin


object AssemblyBukkit : Plugin() {
    override fun onEnable() {
        Assembly.register{ sender,packet ->
            AssemblyPacketReceiveEvent(packet,sender).call()
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(bukkitPlugin, Assembly.CHANNEL){ channel, player, bytes->
            if (channel == Assembly.CHANNEL){
                val buf = Unpooled.wrappedBuffer(bytes)
                val obj = buf.array().decode()
                Assembly.callReceivePacket(player.asPacketSender,obj)
            }
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(bukkitPlugin, Assembly.CHANNEL)

    }
    private val cache = arrayListOf<Player>()
    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        e.player.invokeMethod<Any?>("addChannel", Assembly.CHANNEL)
    }
}