package net.bxx2004.assembly.network.packet

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.AssemblyIdentifier
import java.util.*

/**
 * @author 6hisea
 * @date  2025/10/15 19:43
 * @description: None
 */
data class AssemblyPacketMeta(
    val id: AssemblyIdentifier,
    var timestamp: Long = System.currentTimeMillis(),
    var type: AssemblyPacketType = AssemblyPacketType.NORMAL,
    var transaction: UUID = UUID.randomUUID()
){
    fun update(transaction:UUID){
        timestamp = System.currentTimeMillis()
        this.transaction = transaction
    }

}
