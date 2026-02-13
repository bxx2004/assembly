package net.bxx2004.assembly.core.tool

import net.bxx2004.assembly.utils.BreakUtils.gson

/**
 * @author 6hisea
 * @date  2026/2/10 17:08
 * @description: None
 */
class DataReader(val data:Map<String,Any?>) {
    inline fun <reified T>read(key: String):T?{
        if (!data.containsKey(key)){
            return null
        }
        return gson.fromJson(gson.toJson(data[key]),T::class.java)
    }
    inline fun <reified T>read(key: String,default:T):T{
        if (!data.containsKey(key)){
            return default
        }
        return gson.fromJson(gson.toJson(data[key]),T::class.java)
    }
}