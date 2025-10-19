package net.bxx2004.assembly.network.controller

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.packet.AssemblyPacketType
import net.bxx2004.assembly.network.packet.BreakAssemblyPacket
import net.bxx2004.assembly.utils.BreakUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 6hisea
 * @date  2025/10/2 19:30
 * @description: None
 */
object BreakDataManager {
    private val packets = ConcurrentHashMap<UUID, ArrayList<BreakAssemblyPacket>>()
    fun put(sender: PacketSender,packet: BreakAssemblyPacket) {
        if (packet.meta.type == AssemblyPacketType.BREAK_START) {
            packets[packet.uuid] = ArrayList()
            packets[packet.uuid]?.add(packet)
        }
        if (packet.meta.type == AssemblyPacketType.BREAK) {
            packets[packet.uuid]?.add(packet)
        }
        if (packet.meta.type == AssemblyPacketType.BREAK_END) {
            packets[packet.uuid]?.add(packet)
            val endPacket = BreakUtils.merge(packets[packet.uuid]!!)
            Assembly.callReceivePacket(sender,endPacket)
            packets.remove(packet.uuid)
        }
    }
}