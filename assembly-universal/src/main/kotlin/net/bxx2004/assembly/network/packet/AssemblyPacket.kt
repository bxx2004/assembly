package net.bxx2004.assembly.network.packet

import com.google.gson.JsonObject
import net.bxx2004.assembly.utils.BreakUtils.gson

/**
 * @author 6hisea
 * @date  2025/10/2 18:08
 * @description: None
 */
open class AssemblyPacket(open val meta: AssemblyPacketMeta){
    private val data = HashMap<String, Any>()
    fun write(key:String, value:Any) : AssemblyPacket {
        data.put(key, value)
        return this
    }
    fun <T>read(key:String): T {
        return data.get(key) as T
    }
    fun has(key:String): Boolean {
        return data.containsKey(key)
    }
    fun debug(){
        println(gson.toJson(this))
    }
    fun encode():ByteArray {
        return gson.toJson(this).toByteArray()
    }
    companion object{
        private fun withdraw(s:String): String{
            if (s.startsWith("h")){
                return s.replaceFirst("h","")
            }
            return s
        }
        fun ByteArray.decode():AssemblyPacket{
            val res = withdraw(this.decodeToString())
            val obj = gson.fromJson(res, JsonObject::class.java)
            if (obj.getAsJsonObject("meta").get("type").asString.contains("BREAK")){
                return gson.fromJson(res, BreakAssemblyPacket::class.java)
            }
            return gson.fromJson(res, AssemblyPacket::class.java)
        }
    }
}