package net.bxx2004.assembly_neoforge

import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.application.resource.Gifer
import net.bxx2004.assembly.application.resource.Imager.toPng
import net.bxx2004.assembly.application.resource.toGifer
import net.bxx2004.assembly_neoforge.api.AssemblyResourceNotfoundEvent
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.server.packs.resources.Resource
import net.neoforged.neoforge.common.NeoForge
import java.io.InputStream
import java.lang.AutoCloseable
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 6hisea
 * @date  2025/10/25 16:33
 * @description: None
 */
object AssemblyResourcePack{
    private val cachePool = ConcurrentHashMap<ResourceLocation, AutoCloseable>()
    fun ClientResourceManager.getIoSupplier(
        rl: ResourceLocation
    ): IoSupplier<InputStream>? {
        if (!checkResource(rl.path)){
            NeoForge.EVENT_BUS.post(AssemblyResourceNotfoundEvent(path = rl.path))
            return null
        }
        return IoSupplier<InputStream>{
            when (rl.path.split(".").last()) {
                "jpg","webp","tif" -> findResourceAsStream(path = rl.path).toPng()
                "gif" -> {
                    val gif = (cachePool.computeIfAbsent(rl){
                        findResourceAsStream(path = rl.path).toGifer(true)
                    } as Gifer)
                    if (gif.isClosed()){
                        cachePool[rl] = findResourceAsStream(path = rl.path).toGifer(true)
                    }
                    (cachePool[rl] as Gifer).nextFrame()
                }
                else -> {
                    findResourceAsStream(path = rl.path)
                }
            }


        }
    }
    fun ClientResourceManager.getResource(
        rl: ResourceLocation
    ): Resource? {
        val stream = getIoSupplier(rl)
        if (stream == null) return null
        return Resource(Minecraft.getInstance().vanillaPackResources,stream)
    }
    fun ClientResourceManager.getIoSupplier(
        rl: String
    ): IoSupplier<InputStream>? {
        if (!checkResource(rl)){
            NeoForge.EVENT_BUS.post(AssemblyResourceNotfoundEvent(path = rl))
            return null
        }
        return IoSupplier<InputStream>{
            findResourceAsStream(path = rl)
        }
    }
    fun ClientResourceManager.getResource(
        rl: String
    ): Resource? {
        val stream = getIoSupplier(rl)
        if (stream == null) return null
        return Resource(Minecraft.getInstance().vanillaPackResources,stream)
    }

}