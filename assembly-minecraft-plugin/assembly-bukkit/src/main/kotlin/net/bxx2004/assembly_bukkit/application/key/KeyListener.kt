package net.bxx2004.assembly_bukkit.application.key

import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstanceFromLocal
import net.bxx2004.assembly.application.server.ServerInstanceManager.sync
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import net.bxx2004.assembly_bukkit.api.AssemblyRegisterEvent
import net.bxx2004.assembly_bukkit.api.PlayerConnectionEvent
import net.bxx2004.assembly_minecraft.application.key.KeyApplication
import net.bxx2004.assembly_minecraft.application.key.instances.SimpleKey
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author 6hisea
 * @date  2026/2/11 10:44
 * @description: None
 */
object KeyListener {
    @SubscribeEvent
    fun reg(e: AssemblyRegisterEvent){
        e.register.application(KeyApplication)
            .registerServerInstanceFromLocal<SimpleKey>("key/simple")
    }
    @SubscribeEvent
    fun sync(e: PlayerConnectionEvent.Finish){
        KeyApplication.sync(e.player.asPacketSender)
    }
}