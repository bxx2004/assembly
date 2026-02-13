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
import java.io.*
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
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
    private val packPath = lazy { File(Assembly.DATA_DIR, "resource_pack.zip") }
    val missingResources = CopyOnWriteArraySet<String>()
    override fun onReceive(
        sender: PacketSender,
        packet: AssemblyPacket
    ) {
        packet.bind<ResourceKey> {
            this@ClientResourceManager.key = this.key
            this@ClientResourceManager.password = this.password
        }
    }
    private fun openHtmlFile(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            System.err.println("文件不存在: " + file.getAbsolutePath())
            return
        }

        val absolutePath = file.getAbsolutePath()
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec(
                    arrayOf<String>(
                        "rundll32", "url.dll,FileProtocolHandler", absolutePath
                    )
                )
            } else if (os.contains("mac") || os.contains("darwin")) {
                Runtime.getRuntime().exec(
                    arrayOf<String>(
                        "open", absolutePath
                    )
                )
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                Runtime.getRuntime().exec(
                    arrayOf<String>(
                        "xdg-open", absolutePath
                    )
                )
            } else {
                System.err.println("不支持的操作系统: $os")
                return
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun showMissingResources() {
        missingResources.removeIf { checkResource(it) }
        if (missingResources.isEmpty()) {
            return
        }
        val html = getTemplate()
        val file = File("missingResources.html")

        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(html)
        openHtmlFile(file.path)
    }

    private fun getTemplate():String{
        var prefix = "["
        var suffix = "]"

        missingResources.forEach {
            prefix += "\"$it\","
        }
        prefix = prefix.removeSuffix(",")
        prefix += suffix
        val res = """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>Minecraft - Missing Files</title>
              <style>
                @import url('https://fonts.googleapis.com/css2?family=Minecraftia&display=swap');

                body {
                  background-color: #3a3a3a;
                  color: #e0e0e0;
                  font-family: 'Minecraftia', monospace;
                  margin: 0;
                  padding: 20px;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                  min-height: 100vh;
                }

                .container {
                  background-color: #5a5a5a;
                  border: 4px solid #8b5a21;
                  padding: 20px;
                  max-width: 600px;
                  width: 100%;
                  box-shadow: 0 0 20px rgba(0, 0, 0, 0.7);
                }

                h1 {
                  text-align: center;
                  color: #55ff55;
                  font-size: 24px;
                  margin-bottom: 20px;
                  text-shadow: 2px 2px 0 #000;
                }

                .lang-switch {
                  text-align: center;
                  margin-bottom: 15px;
                }

                .lang-btn {
                  background: #654321;
                  border: 2px solid #8b5a2b;
                  color: #fff;
                  font-family: 'Minecraftia', monospace;
                  padding: 6px 12px;
                  margin: 0 5px;
                  cursor: pointer;
                  font-size: 14px;
                }

                .lang-btn.active {
                  background: #55ff55;
                  color: #000;
                }

                .missing-list {
                  list-style: none;
                  padding: 0;
                }

                .missing-item {
                  background-color: #444;
                  border: 2px solid #654321;
                  margin-bottom: 8px;
                  padding: 10px;
                  font-size: 16px;
                  color: #ff5555;
                }

                .footer {
                  text-align: center;
                  margin-top: 20px;
                  font-size: 14px;
                  color: #aaa;
                }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="lang-switch">
                  <button class="lang-btn active" data-lang="zh">中文</button>
                  <button class="lang-btn" data-lang="en">English</button>
                </div>

                <h1 id="title">⚠️ 缺失文件列表</h1>
                <ul class="missing-list" id="file-list"></ul>
                <div class="footer" id="footer">
                  游戏无法正常加载部分资源。请检查资源包或重新安装。
                </div>
              </div>

              <script>
                // ✅ 共享的缺失文件列表（无需翻译）
                const missingFiles = ${prefix};

                // 🌐 仅 UI 文本需要多语言
                const uiText = {
                  zh: {
                    title: "⚠️ 缺失文件列表",
                    footer: "游戏无法正常加载部分资源。请检查资源包或重新安装。"
                  },
                  en: {
                    title: "⚠️ Missing Files List",
                    footer: "Game failed to load some resources. Please check your resource pack or reinstall."
                  }
                };

                let currentLang = 'zh';

                // 渲染文件列表（只做一次，或语言切换时重用）
                function renderFileList() {
                  const listEl = document.getElementById('file-list');
                  listEl.innerHTML = '';
                  missingFiles.forEach(file => {
                    const li = document.createElement('li');
                    li.className = 'missing-item';
                    li.textContent = file;
                    listEl.appendChild(li);
                  });
                }

                // 更新 UI 语言
                function updateLanguage(lang) {
                  currentLang = lang;
                  const texts = uiText[lang];
                  document.getElementById('title').textContent = texts.title;
                  document.getElementById('footer').textContent = texts.footer;

                  // 更新按钮状态
                  document.querySelectorAll('.lang-btn').forEach(btn => {
                    btn.classList.toggle('active', btn.dataset.lang === lang);
                  });
                }

                // 绑定语言切换
                document.querySelectorAll('.lang-btn').forEach(btn => {
                  btn.addEventListener('click', () => {
                    updateLanguage(btn.dataset.lang);
                  });
                });

                // 初始化
                renderFileList();
                updateLanguage(currentLang);
              </script>
            </body>
            </html>
        """.trimIndent()
        return res
    }

    private fun isReady(): Boolean {
        return key != "#empty" && password != "#empty"
    }

    fun checkResource(path: String): Boolean {
        return try {
            val zipFile = ZipFile(packPath.value, password.toCharArray())
            val targetHeader = try {
                zipFile.getFileHeader(path)
            }catch (e:Exception){
                e.printStackTrace()
                null
            }
            targetHeader != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun findResourceAsStream(path: String): InputStream{
        if (!isReady()) throw IllegalStateException("Resource not ready")
        return try {
            val zipFile = ZipFile(packPath.value, password.toCharArray())
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