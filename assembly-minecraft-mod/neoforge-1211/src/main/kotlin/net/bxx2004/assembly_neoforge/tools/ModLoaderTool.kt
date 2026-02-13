package net.bxx2004.assembly_neoforge.tools

import net.bxx2004.assembly.core.tool.DataReader
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.network.controller.PacketSender
import net.neoforged.fml.ModList
/**
 * @author 6hisea
 * @date  2026/2/10 17:13
 * @description: None
 */
object ModLoaderTool : Tool() {
    override val name: String = "mod-loader"
    override val version: String = "1.0.0"
    override val provider: String = "NeoForge | ModLoader"
    fun getModList(sender: PacketSender,reader: DataReader): List<String>{
        return ModList.get().mods.map { it.modId }
    }
}