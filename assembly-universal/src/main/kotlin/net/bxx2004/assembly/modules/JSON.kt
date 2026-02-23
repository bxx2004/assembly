package net.bxx2004.assembly.modules

import net.bxx2004.assembly.utils.BreakUtils.gson
import net.bxx2004.script.module.IModule

/**
 * @author 6hisea
 * @date  2026/1/5 14:45
 * @description: None
 */
object JSON  : IModule() {
    override val isInject: Boolean = true
    override fun name(): Array<String> {
        return arrayOf("JSON","json")
    }
    fun load(s: String):Any{
        return gson.fromJson(s,Any::class.java)
    }
    fun dump(obj:Any):String{
        return gson.toJson(obj)
    }
}