package net.bxx2004.assembly.network.packet

import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/2 18:41
 * @description: None
 */
data class BreakAssemblyPacket(
    override val meta: AssemblyPacketMeta,
    val uuid: UUID,
    var chunk: ByteArray
) : AssemblyPacket(meta) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreakAssemblyPacket

        if (meta.type != other.meta.type) return false
        if (!chunk.contentEquals(other.chunk)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = meta.type.hashCode()
        result = 31 * result + chunk.contentHashCode()
        return result
    }

}