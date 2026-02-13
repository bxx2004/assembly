package net.bxx2004.assembly_minecraft.application.font

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2026/2/10 18:20
 * @description: None
 */
object FontApplication : AssemblyApplication(){
    override val side: Side
        get() = Side.COMMON
    override val id: AssemblyIdentifier
        get() = "assembly:font".id()
}