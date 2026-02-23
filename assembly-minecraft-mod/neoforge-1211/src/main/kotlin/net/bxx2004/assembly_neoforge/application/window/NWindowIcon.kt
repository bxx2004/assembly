package net.bxx2004.assembly_neoforge.application.window

import com.mojang.blaze3d.platform.IconSet
import com.mojang.blaze3d.platform.MacosUtil
import com.mojang.blaze3d.platform.NativeImage
import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.application.client.Redirect
import net.bxx2004.assembly_minecraft.application.window.instances.WindowIcon
import net.bxx2004.assembly_neoforge.AssemblyResourcePack.getIoSupplier
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

/**
 * @author 6hisea
 * @date  2026/2/23 18:17
 * @description: None
 */
@Redirect()
object NWindowIcon : WindowIcon() {
    override fun mounted() {
        setIcon()
    }

    private fun setIcon() {
        val platform = GLFW.glfwGetPlatform()

        when (platform) {
            // X11 (393217) 和 Wayland (393220) 平台
            393217, 393220 -> setIconForX11Wayland()

            // macOS (393218) 平台
            393218 -> setIconForMacOS()

            // 其他平台不处理
            else -> {}
        }
    }

    private fun setIconForX11Wayland() {
        val iconPath = icon.path
        val resourceStream = ClientResourceManager.findResourceAsStream(iconPath) ?: return

        NativeImage.read(resourceStream).use { nativeImage ->
            val byteBuffer = MemoryUtil.memAlloc(nativeImage.width * nativeImage.height * 4)
            try {
                // 填充像素数据
                byteBuffer.asIntBuffer().put(nativeImage.pixelsRGBA)

                // 创建GLFWImage
                MemoryStack.stackPush().use { stack ->
                    val glfwImage = GLFWImage.malloc(1, stack)
                    glfwImage.apply {
                        it.width(nativeImage.width)
                        it.height(nativeImage.height)
                        it.pixels(byteBuffer)
                    }

                    // 设置窗口图标
                    GLFW.glfwSetWindowIcon(
                        Minecraft.getInstance().window.window,
                        glfwImage
                    )
                }
            } finally {
                // 确保释放ByteBuffer
                MemoryUtil.memFree(byteBuffer)
            }
        }
    }

    private fun setIconForMacOS() {
        val resourceLocation = ResourceLocation.parse(icon.toString())
        val supplier = ClientResourceManager.getIoSupplier(resourceLocation)

        if (supplier != null) {
            MacosUtil.loadIcon(supplier)
        }
    }

    // 扩展函数：简化MemoryStack的关闭
    private inline fun <T> MemoryStack.use(block: (MemoryStack) -> T): T {
        try {
            return block(this)
        } finally {
            close()
        }
    }

    // 扩展函数：简化AutoCloseable的使用
    private inline fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
        var closed = false
        try {
            return block(this)
        } catch (e: Exception) {
            closed = true
            try {
                this?.close()
            } catch (closeException: Exception) {
                e.addSuppressed(closeException)
            }
            throw e
        } finally {
            if (!closed) {
                this?.close()
            }
        }
    }

    override fun unmounted() {
        Minecraft.getInstance().window.setIcon(Minecraft.getInstance().vanillaPackResources, IconSet.RELEASE)
    }
}