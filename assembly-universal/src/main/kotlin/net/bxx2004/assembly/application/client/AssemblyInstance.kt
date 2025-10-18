package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2025/10/18 12:48
 * @description: None
 */
abstract class AssemblyInstance(val id: AssemblyIdentifier): ClientProxy() {
    abstract val resources: List<AssemblyIdentifier>
    abstract fun mounted()
    abstract fun unmounted()
    fun isReady(): Boolean {
        if (Assembly.side != Side.CLIENT) return true
        return resources.all { ClientResourceManager.checkResource(it.path) }
    }
}