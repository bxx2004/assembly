package net.bxx2004.assembly_bukkit.api

import taboolib.common.platform.command.component.CommandBase
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author 6hisea
 * @date  2025/10/26 21:16
 * @description: None
 */
class AssemblyCommandRegisterEvent(): BukkitProxyEvent() {
    private val commands = ArrayList<CommandBase.()-> Unit>()
    fun register(func: CommandBase.()-> Unit) {
        commands.add(func)
    }
    fun getCommands(): List<CommandBase.()-> Unit> {
        
        return commands
    }
}