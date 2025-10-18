package net.bxx2004.assembly.network.controller

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.utils.BreakUtils
import net.bxx2004.assembly.utils.BreakUtils.size
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/2 18:05
 * @description: None
 */
abstract class PacketSender {
    abstract val uuid: UUID
    abstract val name: String
    protected abstract fun sendReload(packet: AssemblyPacket)
    fun send(packet: AssemblyPacket){
        if (packet.size()> Assembly.MAX_PACKET_SIZE){
            BreakUtils.split(packet, Assembly.MAX_PACKET_SIZE).forEach {
                sendReload(it)
            }
        }else{
            sendReload(packet)
        }
    }
    fun send(packet: AssemblyEntity){
        send(packet.toPacket())
    }
    fun response(transaction: UUID, packet: AssemblyPacket){
        packet.meta.update(transaction)
        send(packet)
    }
    fun response(transaction: UUID, packet: AssemblyEntity){
        packet.checkMeta().update(transaction)
        send(packet)
    }
}