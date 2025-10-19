package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.entity.ResourceKey
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.lingala.zip4j.ZipFile
import org.apache.commons.crypto.cipher.CryptoCipher
import org.apache.commons.crypto.stream.CryptoInputStream
import org.apache.commons.crypto.utils.AES
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.util.Properties
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author 6hisea
 * @date  2025/10/18 12:54
 * @description: None
 */
object ClientResourceManager : PacketReceiver{
    private var key = "#empty"
    private var password = "#empty"
    private val packPath = File(Assembly.DATA_DIR, "resource_pack.zip")
    override fun onReceive(
        sender: PacketSender,
        packet: AssemblyPacket
    ) {
        packet.bind<ResourceKey> {
            this@ClientResourceManager.key = this.key
            this@ClientResourceManager.password =this.password
        }
    }

    private fun isReady(): Boolean {
        return key != "#empty" && password != "#empty"
    }

    fun checkResource(path: String): Boolean {
        return try {
            val zipFile = ZipFile(packPath, password.toCharArray())
            val targetHeader = try {
                zipFile.getFileHeader(path)
            }catch (e:Exception){
                null
            }
            targetHeader != null
        } catch (e: Exception) {
            return false
        }
    }

    fun findResourceAsStream(path: String): InputStream{
        if (!isReady()) throw IllegalStateException("Resource not ready")
        return try {
            val zipFile = ZipFile(packPath, password.toCharArray())
            val targetHeader = zipFile.getFileHeader(path)
            decrypt(zipFile.getInputStream(targetHeader))
        } catch (e: Exception) {
            throw RuntimeException("not find resource: $path", e)
        }
    }

    fun decrypt(stream: InputStream): InputStream {
        if (!isReady()) throw IllegalStateException("Resource not ready")
        val bytearray = key.toByteArray()
        val cryptoInputStream = reflectCryptoOutputStream(
            stream,
            SecretKeySpec(bytearray, "AES"),
            IvParameterSpec(bytearray)
        )
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (cryptoInputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        val result = outputStream.toByteArray()
        cryptoInputStream.close()
        stream.close()
        outputStream.close()
        return ByteArrayInputStream(result)
    }
    private fun reflectCryptoOutputStream(stream: InputStream, key: Key, spec: AlgorithmParameterSpec): CryptoInputStream {
        val clazz = Class.forName("org.apache.commons.crypto.cipher.JceCipher")
        var obj = clazz.getDeclaredConstructor(Properties::class.java, String::class.java)
        obj.isAccessible = true
        var ciper = obj.newInstance(Properties(), AES.CBC_PKCS5_PADDING)
        val streamCzz = CryptoInputStream::class.java.getDeclaredConstructor(
            InputStream::class.java,
            CryptoCipher::class.java,
            Int::class.java,
            Key::class.java,
            AlgorithmParameterSpec::class.java
        )
        streamCzz.isAccessible = true
        val stream = streamCzz.newInstance(stream, ciper, 8192, key, spec)
        return stream
    }


}