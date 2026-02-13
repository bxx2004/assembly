package net.bxx2004.assembly_neoforge.utils

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.minecraft.resources.ResourceLocation

/**
 * @author 6hisea
 * @date  2026/2/10 21:24
 * @description: None
 */
object Adapter {
    fun AssemblyIdentifier.rl(): ResourceLocation{
        return ResourceLocation.parse(this.toString())
    }
}