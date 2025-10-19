package net.bxx2004.assembly.application

import org.apache.commons.crypto.cipher.CryptoCipher
import org.apache.commons.crypto.stream.CryptoOutputStream
import org.apache.commons.crypto.utils.AES
import java.io.*
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author 6hisea
 * @date  2025/2/2 16:08
 * @description: None
 */
object ResourceUtil {
    private fun reflectCryptoOutputStream(
        stream: OutputStream,
        key: Key,
        spec: AlgorithmParameterSpec
    ): CryptoOutputStream {
        val clazz = Class.forName("org.apache.commons.crypto.cipher.JceCipher")
        var obj = clazz.getDeclaredConstructor(Properties::class.java, String::class.java)
        obj.isAccessible = true
        var ciper = obj.newInstance(Properties(), AES.CBC_PKCS5_PADDING)
        val streamCzz = CryptoOutputStream::class.java.getDeclaredConstructor(
            OutputStream::class.java,
            CryptoCipher::class.java,
            Int::class.java,
            Key::class.java,
            AlgorithmParameterSpec::class.java
        )
        streamCzz.isAccessible = true
        val stream = streamCzz.newInstance(stream, ciper, 8192, key, spec)
        return stream
    }

    fun encrypt(file: File,key: String): ByteArray{
        var inputStream: InputStream = FileInputStream(file)
        val outputStream = ByteArrayOutputStream()
        val cryptoOutputStream =
            reflectCryptoOutputStream(
                outputStream, SecretKeySpec(key.toByteArray(), "AES"),
                IvParameterSpec(key.toByteArray())
            )
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            // 将读取的数据写入加密输出流
            cryptoOutputStream.write(buffer, 0, bytesRead)
        }
        cryptoOutputStream.close() // 关闭加密输出流
        inputStream.close() // 关闭输入流
        val r = outputStream.toByteArray()
        outputStream.close()
        return r
    }
    fun File.meta() = length() + lastModified()
    fun File.expand(full: Boolean = false): HashMap<String, String> {
        var list = HashMap<String, String>()
        if (isDirectory) {
            val files = listFiles()
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    list.putAll(files[i].expand(full))
                } else {
                    list[if (full) files[i].name else files[i].name.split(".")[0]] = files[i].absolutePath
                }
            }
        }
        return list
    }
    fun File.dir(): File{
        if (!exists()){
            mkdirs()
        }
        return this
    }
}