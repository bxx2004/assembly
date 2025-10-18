package net.bxx2004.assembly_bukkit.api

import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPlayer
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author 6hisea
 * @date  2025/10/17 16:19
 * @description: None
 */
class AssemblyPacketReceiveEvent(
    val packet: AssemblyPacket,
    val sender: PacketSender
) : BukkitProxyEvent() {
    inline fun <reified T : AssemblyEntity>bind(func:T.(player: Player)->Unit){
        packet.bind<T>{
            func(sender.asPlayer)
        }
    }
    inline fun <reified T : AssemblyEntity>bind(func:T.()->Unit){
        packet.bind<T>(func)
    }
}