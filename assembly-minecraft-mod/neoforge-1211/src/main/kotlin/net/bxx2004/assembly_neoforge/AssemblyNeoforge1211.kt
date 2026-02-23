package net.bxx2004.assembly_neoforge

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.AssemblyRegister
import net.bxx2004.assembly.AssemblyRegister.Companion.registerClientInstance
import net.bxx2004.assembly_minecraft.application.font.FontApplication
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly_minecraft.application.key.KeyApplication
import net.bxx2004.assembly_minecraft.application.window.WindowApplication
import net.bxx2004.assembly_neoforge.api.AssemblyPacketReceiveEvent
import net.bxx2004.assembly_neoforge.api.AssemblyRegisterEvent
import net.bxx2004.assembly_neoforge.application.font.NTrueTypeFont
import net.bxx2004.assembly_neoforge.application.key.NSimpleKey
import net.bxx2004.assembly_neoforge.application.window.NWindowIcon
import net.bxx2004.assembly_neoforge.application.window.NWindowTitle

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.PlayerEvent

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File


@Mod(AssemblyNeoforge1211.ID)
@EventBusSubscriber
object AssemblyNeoforge1211 {
    const val ID = "assembly_neoforge_1211"
    val LOGGER: Logger = LogManager.getLogger(ID)
    @SubscribeEvent
    private fun onClientSetup(event: FMLClientSetupEvent) {
        Assembly.init(Side.CLIENT) { sender, packet ->
            val e = AssemblyPacketReceiveEvent(packet,sender)
            NeoForge.EVENT_BUS.post(e)
        }
        Assembly.DATA_DIR = "assembly-mod"

        File(Assembly.DATA_DIR).mkdirs()

        Assembly.register {
            val e = AssemblyRegisterEvent(this)
            NeoForge.EVENT_BUS.post(e)
            register(this)
        }
    }
    @SubscribeEvent
    private fun quit(e: PlayerEvent.PlayerLoggedOutEvent){
        Assembly.removeAllApplications()
    }
    fun register(reg: AssemblyRegister){
        reg.application(FontApplication)
            .registerClientInstance(NTrueTypeFont::class.java)
        reg.application(KeyApplication)
            .registerClientInstance(NSimpleKey::class.java)
        reg.application(WindowApplication)
            .registerClientInstance(NWindowTitle::class.java)
            .registerClientInstance(NWindowIcon::class.java)
    }
}
