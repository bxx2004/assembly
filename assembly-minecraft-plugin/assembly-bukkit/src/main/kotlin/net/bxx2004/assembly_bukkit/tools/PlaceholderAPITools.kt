package net.bxx2004.assembly_bukkit.tools

import net.bxx2004.assembly.core.tool.DataReader
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPlayer
import org.bukkit.Bukkit
import taboolib.platform.compat.replacePlaceholder

/**
 * @author 6hisea
 * @date  2026/1/25 15:43
 * @description: None
 */
object PlaceholderAPITools : Tool() {
    override val name: String = "papi"
    override val version: String = "1.0.0"
    override val provider: String = "PlaceholderAPI | Bukkit Plugin"

    fun replace(sender: PacketSender,reader: DataReader): String {
        val player = sender.asPlayer
        return reader.read("text","null").replacePlaceholder(player)
    }
}