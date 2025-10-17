package net.bxx2004.assembly_bukkit

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly_bukkit.api.AssemblyPacketSendEvent
import org.bukkit.entity.Player
import taboolib.platform.util.bukkitPlugin
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/2 19:43
 * @description: None
 */
class BukkitSender(val player: Player): PacketSender() {
    override val uuid: UUID
        get() = player.uniqueId
    override val name: String
        get() = player.name

    override fun sendReload(packet: AssemblyPacket) {
        val res = AssemblyPacketSendEvent(packet, this).call()
        if (res){
            player.sendPluginMessage(bukkitPlugin, Assembly.CHANNEL, packet.encode())
        }
    }

    companion object{
        private val maps = hashMapOf<Player, BukkitSender>()
        val Player.asPacketSender: PacketSender
        get() = maps.computeIfAbsent(this) { BukkitSender(this) }

    }

}