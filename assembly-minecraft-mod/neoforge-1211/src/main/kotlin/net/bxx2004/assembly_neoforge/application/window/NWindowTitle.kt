package net.bxx2004.assembly_neoforge.application.window

import net.bxx2004.assembly.application.client.Redirect
import net.bxx2004.assembly_minecraft.application.window.instances.WindowTitle
import net.minecraft.client.Minecraft

/**
 * @author 6hisea
 * @date  2026/2/23 18:17
 * @description: None
 */
@Redirect()
object NWindowTitle : WindowTitle() {
    override fun mounted() {
        Minecraft.getInstance().window.setTitle(title)
    }

}