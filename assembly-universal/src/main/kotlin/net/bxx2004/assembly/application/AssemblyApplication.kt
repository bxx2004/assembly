package net.bxx2004.assembly.application

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.entity.FunctionInvoke
import net.bxx2004.assembly.application.entity.FunctionResponse
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.network.packet.entity.sendWithResponse
import java.lang.reflect.Method
import java.util.concurrent.CompletableFuture

abstract class AssemblyApplication : PacketReceiver{
    abstract val side: Side
    abstract val id:AssemblyIdentifier
    private val functions = ArrayList<Method>()
    init {
        Assembly.addListener(this)
    }
    fun registerFunction(function: Method){
        functions.add(function)
    }

    override fun onReceive(sender: PacketSender, packet: AssemblyPacket) {
        packet.bind<FunctionInvoke> {
            val res = functions.find { it.name == this.name }!!.invoke(null,this.args)
            sender.response(this.meta.transaction, AssemblyEntity.build<FunctionResponse<Any>> {
                this.response = res
            })
        }
    }
    fun <T>requestFunction(sender: PacketSender,name: String, vararg args: Any): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        AssemblyEntity.build<FunctionInvoke>{
            this.name = name
            this.args = args
        }.sendWithResponse<FunctionResponse<T>>(sender){
            future.complete(this.response)
        }
        return future
    }
}