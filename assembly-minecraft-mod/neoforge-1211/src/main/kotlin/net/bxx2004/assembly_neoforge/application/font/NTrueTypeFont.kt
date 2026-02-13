package net.bxx2004.assembly_neoforge.application.font

import com.mojang.blaze3d.font.GlyphProvider
import com.mojang.blaze3d.font.TrueTypeGlyphProvider
import com.mojang.blaze3d.platform.TextureUtil
import com.mojang.blaze3d.systems.RenderSystem
import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.application.client.Redirect
import net.bxx2004.assembly_minecraft.application.font.instances.TrueTypeFont
import net.bxx2004.assembly_neoforge.utils.Adapter.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.font.FontOption
import net.minecraft.client.gui.font.FontSet
import net.minecraft.client.gui.font.providers.FreeTypeUtil
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.freetype.FT_Face
import org.lwjgl.util.freetype.FreeType
import java.io.IOException
import java.nio.ByteBuffer

/**
 * @author 6hisea
 * @date  2026/2/10 21:13
 * @description: None
 */
@Redirect
class NTrueTypeFont : TrueTypeFont() {
    var cache: FontSet? = null

    override fun mounted() {

        if (cache != null) {
            return
        }

        var ft_face: FT_Face? = null
        var bytebuffer: ByteBuffer? = null

        try {

            bytebuffer = TextureUtil.readResource(ClientResourceManager.findResourceAsStream(reference.path))
            bytebuffer.flip()
            synchronized(FreeTypeUtil.LIBRARY_LOCK) {
                MemoryStack.stackPush().use { memorystack ->
                    val pointerbuffer = memorystack.mallocPointer(1)
                    FreeTypeUtil.assertError(
                        FreeType.FT_New_Memory_Face(FreeTypeUtil.getLibrary(), bytebuffer, 0L, pointerbuffer),
                        "Initializing font face"
                    )
                    ft_face = FT_Face.create(pointerbuffer.get())
                }
                val s = FreeType.FT_Get_Font_Format(ft_face)
                if ("TrueType" != s) {
                    throw IOException("Font is not in TTF format, was " + s)
                }

                FreeTypeUtil.assertError(
                    FreeType.FT_Select_Charmap(ft_face, FreeType.FT_ENCODING_UNICODE),
                    "Find unicode charmap"
                )
                val truetypeglyphprovider = TrueTypeGlyphProvider(
                    bytebuffer,
                    ft_face,
                    size,
                    oversample,
                    shiftX,
                    shiftY,
                    excludedCharacters
                )
                cache = FontSet(Minecraft.getInstance().textureManager,id.rl())
                RenderSystem.recordRenderCall {
                    cache?.reload(
                        listOf(
                            GlyphProvider.Conditional(truetypeglyphprovider, FontOption.Filter(
                                mapOf(
                                    FontOption.JAPANESE_VARIANTS to true,
                                    FontOption.UNIFORM to true
                                )
                            ))
                        ),
                        setOf(
                            FontOption.JAPANESE_VARIANTS,
                            FontOption.UNIFORM
                        )
                    )
                    state = true
                }
            }
        } catch (exception: Exception) {
            synchronized(FreeTypeUtil.LIBRARY_LOCK) {
                if (ft_face != null) {
                    FreeType.FT_Done_Face(ft_face)
                }
            }

            MemoryUtil.memFree(bytebuffer)
            throw exception
        }
    }

    // 释放资源的方法
    override fun unmounted() {
        cache?.close()
        cache = null
        state = false
    }
}