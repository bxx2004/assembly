package net.bxx2004.assembly_minecraft.application.window

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2026/2/23 17:03
 * @description: None
 */
object WindowApplication : AssemblyApplication(){
    override val side: Side
        get() = Side.COMMON
    override val id: AssemblyIdentifier
        get() = "assembly:window".id()
}