package net.bxx2004.assembly.application.server

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.ResourceUtil
import net.bxx2004.assembly.application.ResourceUtil.dir
import net.bxx2004.assembly.application.ResourceUtil.expand
import net.bxx2004.assembly.application.entity.ResourceKey
import net.bxx2004.assembly.application.entity.ResourceShowMissing
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File
import java.util.function.Supplier


/**
 * @author 6hisea
 * @date  2025/10/18 13:44
 * @description: None
 */
object ServerResourceManager{
    private var keyProvider = Supplier { "#empty" }
    private var passwordProvider = Supplier { "#empty" }
    fun registerKeyProvider(key: Supplier<String>,password: Supplier<String>) {
        keyProvider = key
        passwordProvider = password
    }
    fun sendResourceKey(sender: PacketSender){
        val entity = AssemblyEntity.build<ResourceKey> {
            this.key = keyProvider.get()
            this.password = passwordProvider.get()
        }
        entity.send(sender)
    }
    fun encrypt(){
        val resourceDir = File(Assembly.DATA_DIR,"resources").dir()
        val encryptResourcesDir = File(Assembly.DATA_DIR, "encrypt_resources")
        encryptResourcesDir.deleteRecursively()
        encryptResourcesDir.mkdirs()
        resourceDir.expand().forEach{name,abs->
            val tarPath =abs.replaceFirst("resources", "encrypt_resources")
            val tarFile = File(tarPath)
            if (!tarFile.parentFile.exists()){
                tarFile.parentFile.mkdirs()
            }
            tarFile.writeBytes(ResourceUtil.encrypt(File(abs), keyProvider.get()))
        }
    }
    fun pack(){
        val zipParameters = ZipParameters()
        zipParameters.isEncryptFiles = true
        zipParameters.encryptionMethod = EncryptionMethod.AES

// Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
        zipParameters.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256
        val encryptResourcesDir = File(Assembly.DATA_DIR, "encrypt_resources").dir()
        val zipFilePath = File(Assembly.DATA_DIR, "resource_pack.zip")
        val password = passwordProvider.get()
        try {
            val zipFile = ZipFile(zipFilePath, password.toCharArray())
            encryptResourcesDir.listFiles()?.forEach {
                if (it.isDirectory){
                    zipFile.addFolder(it,zipParameters)
                }else{
                    zipFile.addFile(it,zipParameters)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun showMissingResources(sender: PacketSender) {
        AssemblyEntity.build<ResourceShowMissing>{  }.send(sender)
    }
}