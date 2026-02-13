package net.bxx2004.assembly_neoforge

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.AssemblyPacket.Companion.decode
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

/**
 * @author 6hisea
 * @date  2025/10/15 21:11
 * @description: None
 */
data class AssemblyPayload(val data: ByteArray) : CustomPacketPayload{
    override fun type(): CustomPacketPayload.Type<AssemblyPayload> {
        return TYPE
    }
    companion object{
        val BYTE_ARRAY: StreamCodec<FriendlyByteBuf, ByteArray> = object : StreamCodec<FriendlyByteBuf, ByteArray> {
            override fun decode(buffer: FriendlyByteBuf): ByteArray {
                val a = buffer.toString(buffer.readerIndex(),buffer.readableBytes(),Charsets.UTF_8)
                val res = a.encodeToByteArray()
                Assembly.callReceivePacket(NeoForgeSender,res.decode())
                buffer.clear()
                return res
            }

            override fun encode(buffer: FriendlyByteBuf, value: ByteArray) {
                buffer.writeBytes(value)
                //FriendlyByteBuf.writeByteArray(buffer, value)
            }
        }


        val TYPE = CustomPacketPayload.Type<AssemblyPayload>(
            ResourceLocation.fromNamespaceAndPath(
                Assembly.CHANNEL.split(":")[0],
                Assembly.CHANNEL.split(":")[1]
            )
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, AssemblyPayload> = StreamCodec.composite(
            BYTE_ARRAY,
            AssemblyPayload::data
        ){
            AssemblyPayload(it)
        }
        fun AssemblyPayload.decodeToPacket() : AssemblyPacket{
            return data.decode()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AssemblyPayload

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
