package net.bxx2004.script.module

import jdk.dynalink.beans.StaticClass
import net.bxx2004.script.ThorCompile
import net.bxx2004.script.ThorShell
import net.bxx2004.script.source.source
import java.io.File

/**
 * JS解释器内顶级安装器变量
 */
class InstallAPI(val shell: ThorShell) {
    val caches = HashMap<String, ThorCompile>()
    private fun _path_(p: String): ThorCompile {
        val f = File(p)
        val r = shell.compile(source(f))
        r.eval()
        return r
    }

    /**
     * 导入Java类
     */
    fun import(clazz:String):StaticClass{
        val cla = Class.forName(clazz)
        return StaticClass.forClass(cla)
    }

    /**
     * 根据路径导入js脚本
     */
    fun path(path: String): Map<String, Any?> {
        return _path_(path).variable()
    }

    /**
     * 导入模块
     */
    fun module(name: String): IModule? {
        IModule.modules.forEach {
            if (it.name().contains(name)) {
                return it
            }
        }
        return null
    }

    override fun toString(): String {
        return "InstallAPI{module,path,import}"
    }
}