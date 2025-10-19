package net.bxx2004.assembly_neoforge

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly_neoforge.api.AssemblyPacketReceiveEvent
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.common.NeoForge
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File


@Mod(AssemblyNeoforge1218.ID)
@EventBusSubscriber
object AssemblyNeoforge1218 {
    const val ID = "assembly_neoforge_1218"
    val LOGGER: Logger = LogManager.getLogger(ID)
    @SubscribeEvent
    private fun onClientSetup(event: FMLClientSetupEvent) {
        Assembly.init(Side.CLIENT) { sender, packet ->
            val e = AssemblyPacketReceiveEvent(packet,sender)
            NeoForge.EVENT_BUS.post(e)
        }
        Assembly.DATA_DIR = "assembly-mod"
        File(Assembly.DATA_DIR).mkdirs()
        LOGGER.log(Level.INFO, "assembly_neoforge_1218...")
    }
}
