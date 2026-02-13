package net.bxx2004.assembly.application.resource

import com.madgag.gif.fmsware.GifDecoder
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.AutoCloseable
import javax.imageio.ImageIO

/**
 * GIF帧序列读取器
 * @author 6hisea
 * @date 2025/10/31 18:10
 */
class Gifer(stream: InputStream, val repeatable: Boolean = true) : AutoCloseable {

    private var decoder: GifDecoder? = GifDecoder().apply {
        read(stream)
    }

    private var currentFrameIndex = 0
    private var lastFrameTime = System.currentTimeMillis()
    private var isClosed = false

    /**
     * 获取下一帧图像
     */
    fun nextFrame(): InputStream {
        check(!isClosed) { "Gifer has been closed" }

        val frameCount = decoder!!.frameCount
        if (frameCount == 0) {
            throw IllegalStateException("No frames available in GIF")
        }

        // 处理帧索引逻辑
        if (currentFrameIndex >= frameCount) {
            if (repeatable) {
                currentFrameIndex = 0
            } else {
                // 非重复模式下返回最后一帧
                return getFrameAsStream(frameCount - 1)
            }
        }

        val currentTime = System.currentTimeMillis()
        val frameDelay = decoder!!.getDelay(currentFrameIndex).toLong()

        // 检查是否到了显示下一帧的时间
        if (currentTime - lastFrameTime >= frameDelay) {
            lastFrameTime = currentTime
            val frame = getFrameAsStream(currentFrameIndex)
            currentFrameIndex++
            return frame
        } else {
            // 时间未到，返回当前帧
            return getFrameAsStream(currentFrameIndex)
        }
    }

    /**
     * 获取当前帧索引
     */
    fun getCurrentFrameIndex(): Int = currentFrameIndex

    /**
     * 获取总帧数
     */
    fun getFrameCount(): Int = decoder!!.frameCount

    /**
     * 重置到第一帧
     */
    fun reset() {
        currentFrameIndex = 0
        lastFrameTime = System.currentTimeMillis()
    }

    /**
     * 获取指定帧的延迟时间(毫秒)
     */
    fun getFrameDelay(frameIndex: Int): Int {
        check(frameIndex in 0 until decoder!!.frameCount) { "Frame index out of bounds" }
        return decoder!!.getDelay(frameIndex)
    }

    /**
     * 直接获取指定帧（不涉及时间逻辑）
     */
    fun getFrame(frameIndex: Int): InputStream {
        check(!isClosed) { "Gifer has been closed" }
        check(frameIndex in 0 until decoder!!.frameCount) { "Frame index out of bounds" }
        return getFrameAsStream(frameIndex)
    }

    private fun getFrameAsStream(frameIndex: Int): InputStream {
        return try {
            val image = decoder!!.getFrame(frameIndex)
            bufferedImageToInputStream(image)
        } catch (e: Exception) {
            throw IOException("Failed to get frame $frameIndex", e)
        }
    }

    private fun bufferedImageToInputStream(image: BufferedImage): InputStream {
        ByteArrayOutputStream().use { os ->
            if (!ImageIO.write(image, "PNG", os)) {
                throw IOException("No appropriate PNG writer found")
            }
            return ByteArrayInputStream(os.toByteArray())
        }
    }

    override fun close() {
        if (!isClosed) {
            isClosed = true
            decoder = null
        }
    }

    /**
     * 检查是否已关闭
     */
    fun isClosed(): Boolean = isClosed
}
fun InputStream.toGifer(repeatable: Boolean): Gifer {
    return Gifer(this, repeatable)
}