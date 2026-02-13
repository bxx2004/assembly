package net.bxx2004.assembly.application.resource

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.AssemblyRegister.Companion.registerServerInstance
import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyApplication.Companion.clearRegisterInstances
import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.ehc.parse
import net.bxx2004.assembly.application.server.ServerInstanceManager.removeAllInstance
import net.bxx2004.assembly.application.server.ServerInstanceManager.sync
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.utils.log
import net.bxx2004.assembly.utils.property
import net.bxx2004.script.container.ScriptContainerProvider
import java.io.File

/**
 * @author 6hisea
 * @date  2026/2/11 17:59
 * @description: None
 */
object InstanceLoader {
    inline fun <reified T: AssemblyInstance>reload(dir:String,app: AssemblyApplication,sender: List<PacketSender> = ArrayList()){
        sender.forEach {
            app.removeAllInstance(it)
        }
        app.clearRegisterInstances()
        load<T>(dir,app)
        sender.forEach {
            app.sync(it)
        }
    }

    inline fun <reified T: AssemblyInstance>load(dir:String,app: AssemblyApplication){
        val files = getAllFile(dir,"conf")
        log("将要注册 ${files.size} 个 ${app.id} 的实例.")
        files.map { parse<T>(it.inputStream()) }.forEach {
            if (it is ScriptContainerProvider){
                it.scriptContainer.property("appId",app.id)
                it.scriptContainer.property("insId",it.id)
                println(it.scriptContainer)
            }

            app.registerServerInstance(it)
        }
    }

    fun getAllFile(dir:String,ext: String = "*"):List<File>{
        val tar = File(Assembly.DATA_DIR, dir)
        if (!tar.exists()){
            tar.mkdirs()
        }
        val res = mutableListOf<File>()
        for (cf in tar.listFiles()){
            if (cf.isDirectory){
                res += getAllFile(cf.absolutePath.split(Assembly.DATA_DIR)[1])
            }else{
                if (ext == "*"){
                    res.add(cf)
                    continue
                }
                if (cf.extension == ext){
                    res.add(cf)
                    continue
                }
            }
        }
        return res
    }
}