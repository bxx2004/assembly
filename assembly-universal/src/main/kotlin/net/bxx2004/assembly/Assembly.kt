package net.bxx2004.assembly

import net.bxx2004.assembly.network.controller.BreakDataManager
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.BreakAssemblyPacket

/**
 * @author 6hisea
 * @date  2025/10/2 17:06
 * @description: None
 */
object Assembly {
    var MAX_PACKET_SIZE = 1048575L
    var CHANNEL = "assembly:network"
    private var receivers:PacketReceiver?=null
    fun register(func:(PacketSender, AssemblyPacket)->Unit) {
        receivers = object : PacketReceiver {
            override fun onReceive(sender: PacketSender,packet: AssemblyPacket) {
                if (packet is BreakAssemblyPacket) {
                    BreakDataManager.put(sender, packet)
                }else{
                    func(sender,packet)
                }
            }
        }
    }
    fun callReceivePacket(sender: PacketSender,receiver: AssemblyPacket) {
        receivers?.onReceive(sender, receiver)
    }
}