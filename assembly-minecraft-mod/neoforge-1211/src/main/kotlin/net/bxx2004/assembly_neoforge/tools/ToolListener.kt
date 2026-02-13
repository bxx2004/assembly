package net.bxx2004.assembly_neoforge.tools

import net.bxx2004.assembly_neoforge.api.AssemblyRegisterEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

/**
 * @author 6hisea
 * @date  2026/2/10 17:12
 * @description: None
 */
@EventBusSubscriber
object ToolListener {
    @SubscribeEvent
    fun onReg(reg: AssemblyRegisterEvent){
        reg.register.tool(ModLoaderTool)
    }
}