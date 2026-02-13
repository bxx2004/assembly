package net.bxx2004.assembly_neoforge.application.key

import net.bxx2004.assembly.application.client.Redirect
import net.bxx2004.assembly_minecraft.application.key.instances.SimpleKey
import net.bxx2004.assembly_neoforge.NeoForgeSender
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallbackI

/**
 * @author 6hisea
 * @date  2026/2/12 16:38
 * @description: None
 */
@Redirect
class NSimpleKey : SimpleKey() {
    var callback = GLFWKeyCallbackI { _, _, _, _, _ -> }
    override fun mounted() {
        callback = object :GLFWKeyCallbackI{
            override fun invoke(window: Long, kk: Int, scancode: Int, action: Int, mods: Int) {
                if (key == kk){
                    if (Minecraft.getInstance().screen != null){
                        if (!screenable){
                            return
                        }
                    }
                    if (action == GLFW.GLFW_PRESS) {
                        scriptContainer.evalClient(NeoForgeSender,"onPress",mapOf(
                            "key" to kk,
                            "scancode" to scancode,
                            "action" to action,
                            "mods" to mods
                        ))
                    }else{
                        scriptContainer.evalClient(NeoForgeSender,"onRelease",mapOf(
                            "key" to kk,
                            "scancode" to scancode,
                            "action" to action,
                            "mods" to mods
                        ))
                    }
                }
            }
        }
    }

    override fun unmounted() {
        callback = GLFWKeyCallbackI { _, _, _, _, _ -> }
    }
}