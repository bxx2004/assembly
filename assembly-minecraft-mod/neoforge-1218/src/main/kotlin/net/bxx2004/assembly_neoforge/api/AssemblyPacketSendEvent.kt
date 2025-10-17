package net.bxx2004.assembly_neoforge.api

import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent


/**
 * @author 6hisea
 * @date  2025/10/17 16:19
 * @description: None
 */
class AssemblyPacketSendEvent(
    val packet: AssemblyPacket,
    val sender: PacketSender
) : Event(), ICancellableEvent {
}