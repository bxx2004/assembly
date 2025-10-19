package net.bxx2004.assembly.network.packet

import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
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
    @Deprecated("unstable")
    fun <T>read(key:String): T {
        return data.get(key) as T
    }
    inline fun <reified T : AssemblyEntity>bind(func:T.()->Unit){
        val clazz = T::class.java
        val obj = clazz.getDeclaredConstructor().newInstance()
        if (meta.id == obj.id()){
            func(toEntity(clazz,obj))
        }
    }
    fun <T : AssemblyEntity> toEntity(clazz: Class<T>,obje:T?=null): T {
        val obj = obje ?: clazz.getDeclaredConstructor().newInstance()
        clazz.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.name != "INSTANCE") {
                when (field.type) {
                    AssemblyPacketMeta::class.java -> {
                        field.set(obj,meta)
                    }
                    Byte::class.java -> {
                        field.set(obj, readByte(field.name))
                    }
                    Byte::class.javaPrimitiveType -> {
                        field.setByte(obj, readByte(field.name))
                    }
                    Short::class.java -> {
                        field.set(obj, readShort(field.name))
                    }
                    Short::class.javaPrimitiveType -> {
                        field.setShort(obj, readShort(field.name))
                    }
                    Int::class.java -> {
                        field.set(obj, readInt(field.name))
                    }
                    Int::class.javaPrimitiveType -> {
                        field.setInt(obj, readInt(field.name))
                    }
                    Long::class.java -> {
                        field.set(obj, readLong(field.name))
                    }
                    Long::class.javaPrimitiveType -> {
                        field.setLong(obj, readLong(field.name))
                    }
                    Float::class.java -> {
                        field.set(obj, readFloat(field.name))
                    }
                    Float::class.javaPrimitiveType -> {
                        field.setFloat(obj, readFloat(field.name))
                    }
                    Double::class.java -> {
                        field.set(obj, readDouble(field.name))
                    }
                    Double::class.javaPrimitiveType -> {
                        field.setDouble(obj, readDouble(field.name))
                    }
                    Boolean::class.java -> {
                        field.set(obj, readBoolean(field.name))
                    }
                    Boolean::class.javaPrimitiveType -> {
                        field.setBoolean(obj, readBoolean(field.name))
                    }
                    Char::class.java -> {
                        field.set(obj, readChar(field.name))
                    }
                    Char::class.javaPrimitiveType -> {
                        field.setChar(obj, readChar(field.name))
                    }
                    String::class.java -> {
                        field.set(obj, readString(field.name))
                    }

                    else -> {
                        val value = read<Any>(field.name)
                        if (value::class.java == LinkedTreeMap::class.java) {
                            field.set(obj, gson.fromJson(gson.toJson(value),field.type))
                        }else{
                            field.set(obj,read(field.name))
                        }
                    }
                }
            }


        }
        return obj
    }
    fun readNumber(key:String): Number {
        return read<Number>(key)
    }
    fun readInt(key:String): Int {
        return read<Number>(key).toInt()
    }
    fun readByte(key:String): Byte {
        return read<Number>(key).toByte()
    }
    fun readShort(key:String): Short {
        return read<Number>(key).toShort()
    }
    fun readLong(key:String): Long {
        return read<Number>(key).toLong()
    }
    fun readFloat(key:String): Float {
        return read<Number>(key).toFloat()
    }
    fun readDouble(key:String): Double {
        return read<Number>(key).toDouble()
    }
    fun readBoolean(key:String): Boolean {
        return read<Boolean>(key)
    }
    fun readChar(key:String): Char {
        return read<Char>(key)
    }
    fun readString(key:String): String {
        return read<String>(key)
    }
    fun has(key:String): Boolean {
        return data.containsKey(key)
    }
    fun debug(){
        println(gson.toJson(this))
    }
    fun json():String {
        return gson.toJson(this)
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