package net.bxx2004.assembly_bukkit.api

import taboolib.platform.type.BukkitProxyEvent
import java.io.File

/**
 * @author 6hisea
 * @date  2025/10/28 20:11
 * @description: None
 */
object AssemblyResourcePackEvent{
    class Start(): BukkitProxyEvent()
    class Finish(val resourcePack: File): BukkitProxyEvent()
}