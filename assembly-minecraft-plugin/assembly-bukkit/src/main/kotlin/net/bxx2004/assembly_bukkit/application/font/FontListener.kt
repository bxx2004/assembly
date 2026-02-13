package net.bxx2004.assembly_bukkit.application.font

import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstanceFromLocal
import net.bxx2004.assembly_minecraft.application.font.instances.TrueTypeFont
import net.bxx2004.assembly_minecraft.application.font.FontApplication
import net.bxx2004.assembly.application.server.ServerInstanceManager.sync
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import net.bxx2004.assembly_bukkit.api.AssemblyRegisterEvent
import net.bxx2004.assembly_bukkit.api.PlayerConnectionEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author 6hisea
 * @date  2026/2/11 10:44
 * @description: None
 */
object FontListener {
    @SubscribeEvent
    fun reg(e: AssemblyRegisterEvent){
        e.register.application(FontApplication)
            .registerServerInstanceFromLocal<TrueTypeFont>("font/ttf")
    }
    @SubscribeEvent
    fun sync(e: PlayerConnectionEvent.Finish){
        FontApplication.sync(e.player.asPacketSender)
    }
}