package net.bxx2004.assembly_bukkit.api

import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author 6hisea
 * @date  2025/10/26 21:16
 * @description: None
 */
class AssemblyCommandRegisterEvent(): BukkitProxyEvent() {
    private val commands = ArrayList<CommandComponentLiteral.()-> Unit>()
    fun register(func: CommandComponentLiteral.()-> Unit) {
        commands.add(func)
    }
    fun getCommands(): List<CommandComponentLiteral.()-> Unit> {
        
        return commands
    }
}