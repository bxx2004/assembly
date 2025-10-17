package net.bxx2004.assembly_neoforge

import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly_neoforge.api.AssemblyPacketSendEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.neoforged.neoforge.common.NeoForge
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/15 12:35
 * @description: None
 */
object NeoForgeSender : PacketSender(){

    override val name: String
        get() = Minecraft.getInstance().player!!.name.string
    override val uuid: UUID
        get() = Minecraft.getInstance().player!!.uuid

    override fun sendReload(packet: AssemblyPacket) {
        val e = AssemblyPacketSendEvent(packet,this)
        val res = NeoForge.EVENT_BUS.post(e)
        if (!res.isCanceled){
            Minecraft.getInstance().connection?.send(ServerboundCustomPayloadPacket(AssemblyPayload(packet.encode())))
        }
    }
}