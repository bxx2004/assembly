package net.bxx2004.assembly_bukkit

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly_bukkit.api.AssemblyPacketSendEvent
import net.bxx2004.script.ThorExecutor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.bukkitPlugin
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/2 19:43
 * @description: None
 */
class BukkitSender(val player: Player): PacketSender(), ThorExecutor {
    override val uuid: UUID
        get() = player.uniqueId
    override val name: String
        get() = player.name

    override fun sendReload(packet: AssemblyPacket) {
        val res = AssemblyPacketSendEvent(packet, this).call()
        if (res){
            //player.invokeMethod<Any?>("sendCustomPayload", MinecraftKey.parse(Assembly.CHANNEL),packet.encode())
            player.sendPluginMessage(bukkitPlugin, Assembly.CHANNEL, packet.encode())
        }
    }

    override fun tell(message: String) {
        player.sendMessage(message)
    }

    override fun adapt(): Any {
        return player
    }

    override fun name(): String {
        return name
    }

    override fun platform(): String {
        return "Bukkit"
    }

    companion object{
        private val maps = hashMapOf<Player, BukkitSender>()
        val Player.asPacketSender: PacketSender
        get() = maps.computeIfAbsent(this) { BukkitSender(this) }
        val PacketSender.asPlayer: Player
            get() = Bukkit.getPlayer(this.name)!!

        @SubscribeEvent
        fun onQuit(event: PlayerQuitEvent) {
            if (maps.containsKey(event.player)) {
                maps.remove(event.player)
            }
        }
        @SubscribeEvent
        fun onJoin(event: PlayerJoinEvent) {
            if (maps.containsKey(event.player)) {
                maps.remove(event.player)
            }
        }
    }

}