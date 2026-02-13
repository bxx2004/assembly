package net.bxx2004.assembly.application.resource

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * @author 6hisea
 * @date  2025/10/31 19:54
 * @description: None
 */
object Imager {
    fun InputStream.toPng(): InputStream {
        return use { inputStream ->
            val image = ImageIO.read(inputStream)
            ByteArrayOutputStream().use { outputStream ->
                if (!ImageIO.write(image, "PNG", outputStream)) {
                    throw IllegalStateException("PNG格式转换失败")
                }
                ByteArrayInputStream(outputStream.toByteArray())
            }
        }
    }
}