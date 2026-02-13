package net.bxx2004.assembly_bukkit.tools

import net.bxx2004.assembly.core.tool.DataReader
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.platform.compat.getBalance

/**
 * @author 6hisea
 * @date  2026/1/25 19:16
 * @description: None
 */
object VaultTools : Tool() {
    override val name: String = "vault"
    override val version: String = "1.0.0"
    override val provider: String = "Vault | Bukkit Plugin"
    fun getBalance(sender: PacketSender,reader: DataReader): Double {
        val player = sender.asPlayer
        return player.getBalance()
    }
    fun hasPermission(sender: PacketSender,reader: DataReader):Boolean {
        return sender.asPlayer.hasPermission(reader.read<String>("permission","no-permission@assembly"))
    }
}