package net.bxx2004.assembly.network.packet

import net.bxx2004.assembly.data.AssemblyIdentifier

/**
 * @author 6hisea
 * @date  2025/10/15 19:43
 * @description: None
 */
data class AssemblyPacketMeta(
    val id: AssemblyIdentifier,
    val timestamp: Long = System.currentTimeMillis(),
    var type: AssemblyPacketType = AssemblyPacketType.NORMAL
)