package net.bxx2004.assembly.utils

import com.google.gson.Gson
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.network.packet.AssemblyPacketType
import net.bxx2004.assembly.network.packet.BreakAssemblyPacket
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/2 18:29
 * @description: None
 */
object BreakUtils {
    val gson = Gson()
    fun AssemblyPacket.size(): Long{
        return gson.toJson(this, AssemblyPacket::class.java).encodeToByteArray().size.toLong()
    }
    fun split(packet: AssemblyPacket, size: Long = 1048575L): List<BreakAssemblyPacket> {
        val uuid = UUID.randomUUID()
        val bytes = gson.toJson(packet, AssemblyPacket::class.java).encodeToByteArray()
        val res = bytes.chunked(size).map { chunk ->
            BreakAssemblyPacket(AssemblyPacketMeta(packet.meta.id, type = AssemblyPacketType.BREAK), uuid, chunk)
        }.toMutableList()

        // 设置第一个包为开始类型
        if (res.isNotEmpty()) {
            res[0].meta.type = AssemblyPacketType.BREAK_START
        }

        // 设置最后一个包为结束类型
        if (res.size > 1) {
            res[res.size - 1].meta.type = AssemblyPacketType.BREAK_END
        }

        return res
    }

    /**
     * 将分片的包合并成原始包
     */
    fun merge(packets: List<BreakAssemblyPacket>): AssemblyPacket {
        if (packets.isEmpty()) {
            throw IllegalArgumentException("Packet list cannot be empty")
        }

        // 验证所有包是否属于同一个分组
        val firstId = packets.first().meta.id
        if (!packets.all { it.meta.id == firstId }) {
            throw IllegalArgumentException("All packets must have the same ID to be merged")
        }

        // 验证包的顺序和完整性
        validatePacketSequence(packets)

        // 合并数据
        val allData = packets.flatMap {
            it.chunk.toList()
        }.toByteArray()

        // 尝试反序列化
        val jsonString = String(allData, Charsets.UTF_8)
        try {
            return gson.fromJson(jsonString, AssemblyPacket::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Failed to merge packets: ${e.message}", e)
        }
    }

    /**
     * 验证包序列的完整性
     */
    private fun validatePacketSequence(packets: List<BreakAssemblyPacket>) {
        if (packets.isEmpty()) return

        // 检查第一个包是否是BREAK_START或NORMAL
        val firstType = packets.first().meta.type
        if (firstType != AssemblyPacketType.BREAK_START && firstType != AssemblyPacketType.NORMAL) {
            throw IllegalArgumentException("First packet must be of type BREAK_START or NORMAL, but was $firstType")
        }

        // 检查最后一个包是否是BREAK_END或NORMAL
        val lastType = packets.last().meta.type
        if (lastType != AssemblyPacketType.BREAK_END && lastType != AssemblyPacketType.NORMAL) {
            throw IllegalArgumentException("Last packet must be of type BREAK_END or NORMAL, but was $lastType")
        }

        // 如果是分片包，验证中间包类型应为BREAK
        if (firstType == AssemblyPacketType.BREAK_START || lastType == AssemblyPacketType.BREAK_END) {
            for (i in 1 until packets.size - 1) {
                if (packets[i].meta.type != AssemblyPacketType.BREAK) {
                    throw IllegalArgumentException("Middle packet at index $i should be of type BREAK, but was ${packets[i].meta.type}")
                }
            }
        }
    }
    private fun ByteArray.chunked(size: Long): List<ByteArray> {
        if (size <= 0) {
            throw IllegalArgumentException("Chunk size must be positive, but was $size")
        }

        if (this.isEmpty()) {
            return emptyList()
        }

        val result = mutableListOf<ByteArray>()
        var startIndex = 0

        while (startIndex < this.size) {
            val endIndex = minOf(startIndex + size.toInt(), this.size)
            val chunk = ByteArray(endIndex - startIndex)
            System.arraycopy(this, startIndex, chunk, 0, chunk.size)
            result.add(chunk)
            startIndex = endIndex
        }

        return result
    }
}