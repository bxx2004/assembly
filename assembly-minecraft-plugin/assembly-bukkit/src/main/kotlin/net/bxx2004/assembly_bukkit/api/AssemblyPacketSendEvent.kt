package net.bxx2004.assembly_bukkit.api

import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author 6hisea
 * @date  2025/10/17 16:19
 * @description: None
 */
class AssemblyPacketSendEvent(
    val packet: AssemblyPacket,
    val sender: PacketSender
) : BukkitProxyEvent() {
}