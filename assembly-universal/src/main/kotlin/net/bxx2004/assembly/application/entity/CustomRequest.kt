package net.bxx2004.assembly.application.entity

import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.utils.BreakUtils.gson

/**
 * @author 6hisea
 * @date  2026/1/5 14:06
 * @description: None
 */
class CustomRequest : AssemblyEntity {
    override fun id(): AssemblyIdentifier {
        return "application:c2s_request".id()
    }
    var path = "default"
    var data = mapOf<String, Any?>()
    lateinit var meta: AssemblyPacketMeta

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