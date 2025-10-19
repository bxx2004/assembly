package net.bxx2004.assembly.network.controller

import net.bxx2004.assembly.network.packet.AssemblyPacket

/**
 * @author 6hisea
 * @date  2025/10/2 18:29
 * @description: None
 */
interface PacketReceiver {
    fun onReceive(sender: PacketSender,packet: AssemblyPacket)
}