package net.bxx2004.assembly_bukkit.api

import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent


/**
 * @author 6hisea
 * @date  2025/10/22 15:29
 * @description: None
 */
object PlayerConnectionEvent {
    class Start(val player: Player): BukkitProxyEvent()
    class Finish(val player: Player): BukkitProxyEvent()
}