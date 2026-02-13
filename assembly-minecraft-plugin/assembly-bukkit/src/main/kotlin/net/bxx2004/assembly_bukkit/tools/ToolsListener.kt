package net.bxx2004.assembly_bukkit.tools

import net.bxx2004.assembly_bukkit.api.AssemblyRegisterEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author 6hisea
 * @date  2026/1/25 15:40
 * @description: None
 */
object ToolsListener {
    @SubscribeEvent
    fun onRegister(e: AssemblyRegisterEvent){
        e.register.tool(PlaceholderAPITools)
        e.register.tool(VaultTools)
    }
}