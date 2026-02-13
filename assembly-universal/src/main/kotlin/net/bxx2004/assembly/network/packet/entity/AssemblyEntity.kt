package net.bxx2004.assembly.network.packet.entity

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.AssemblyPacketMeta
import net.bxx2004.assembly.utils.BreakUtils.gson

/**
 * @author 6hisea
 * @date  2025/10/17 21:55
 * @description: None
 */
interface AssemblyEntity {
    fun id():AssemblyIdentifier

    private fun setMeta(){
        val meta = this::class.java.declaredFields.find { it.name == "meta" }!!
        if (meta.get(this) != null) return
        meta.isAccessible = true
        meta.set(this,AssemblyPacketMeta(id()))
    }
    fun toPacket(): AssemblyPacket {
        val res = AssemblyPacket(
            (this::class.java.declaredFields.find { it.name == "meta" }?.get(this)?:AssemblyPacketMeta(id())) as AssemblyPacketMeta
        )
        this::class.java.declaredFields.forEach { field ->
            if (field.name != "INSTANCE"){
                field.isAccessible = true
                res.write(field.name,field.get(this))
            }
        }
        return res
    }
    fun send(sender: PacketSender) {
        sender.send(this)
    }

    fun checkMeta(): AssemblyPacketMeta{
        return try {
            setMeta()
            this::class.java.declaredFields.find { it.name == "meta" }!!.get(this) as AssemblyPacketMeta
        }catch (e:Exception){
            throw RuntimeException("entity must declared `meta` if you want to receive response.")
        }
    }

    companion object{
        inline fun <reified T: AssemblyEntity>build(func:T.()-> Unit):T{
            val obj = T::class.java.getDeclaredConstructor().newInstance()
            func(obj)
            return obj
        }
        inline fun <reified T: AssemblyEntity>build(jsonString: String):T{
            return gson.fromJson(jsonString,T::class.java)
        }
    }
}

inline fun <reified T: AssemblyEntity> AssemblyEntity.sendWithResponse(sender: PacketSender, crossinline func: T.() -> Unit) {
    val meta = checkMeta()
    Assembly.listenNextTransaction(meta.transaction){ sender,packet->
        packet.bind<T>(func)
    }
    send(sender)
}
inline fun <reified T: AssemblyEntity> AssemblyEntity.sendWithResponse(sender: PacketSender, timeout:Long,
                                                                       noinline timeFunc:()->Unit, crossinline func: T.() -> Unit) {
    val meta = checkMeta()
    Assembly.listenNextTransaction(meta.transaction,timeout,timeFunc){ sender,packet->
        packet.bind<T>(func)
    }
    send(sender)
}