package net.bxx2004.assembly.core.tool

import net.bxx2004.assembly.data.anno.Unsafe
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.network.packet.entity.sendWithResponse
import java.util.concurrent.CompletableFuture

/**
 * @author 6hisea
 * @date  2026/2/7 12:01
 * @description: None
 */
abstract class Tool {
    abstract val provider : String
    abstract val version: String
    abstract val name: String

    fun invoke(sender: PacketSender,name: String,data:Map<String, Any?>):Any?{
        val r = this::class.java.getDeclaredMethod(name, PacketSender::class.java, DataReader::class.java).let {
            it.isAccessible = true
            it.invoke(this@Tool,sender, DataReader(data))
        }
        return r
    }

    companion object{
        @Unsafe("注意阻塞")
        fun delegate(sender: PacketSender,toolName: String,methodName: String,data:Map<String,Any?>) : CompletableFuture<Any?>{
            var res = CompletableFuture<Any?>()
            AssemblyEntity.build<ToolCallingRequest> {
                this.toolName = toolName
                this.methodName = methodName
                this.data = data
            }.sendWithResponse<ToolCallingResponse>(sender,60000L,{
                res.complete(null)
            },{
                res.complete(result)
            })
            return res
        }
    }
}