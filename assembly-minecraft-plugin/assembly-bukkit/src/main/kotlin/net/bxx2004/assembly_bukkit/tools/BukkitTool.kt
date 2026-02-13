package net.bxx2004.assembly_bukkit.tools

import net.bxx2004.assembly.core.tool.DataReader
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.network.controller.PacketSender
import org.bukkit.Bukkit

/**
 * @author 6hisea
 * @date  2026/2/10 18:00
 * @description: None
 */
object BukkitTool : Tool(){
    override val name: String = "bukkit"
    override val version: String = "1.0.0"
    override val provider: String = "Bukkit | Bukkit"
    fun getPlayerList(sender: PacketSender,reader: DataReader): List<String> {
        return Bukkit.getOnlinePlayers().map { player -> player.name}
    }
}