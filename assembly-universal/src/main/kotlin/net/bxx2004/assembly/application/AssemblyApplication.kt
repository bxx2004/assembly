package net.bxx2004.assembly.application

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.client.ClientInstanceManager
import net.bxx2004.assembly.application.client.ClientInstanceManager.addClientInstance
import net.bxx2004.assembly.application.client.ClientInstanceManager.removeAllClientInstance
import net.bxx2004.assembly.application.client.ClientInstanceManager.removeClientInstanceById
import net.bxx2004.assembly.application.entity.*
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.assembly.network.packet.entity.sendWithResponse
import java.io.File
import java.lang.reflect.Method
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

abstract class AssemblyApplication : PacketReceiver{
    companion object{
        val AssemblyApplication.instances: List<AssemblyInstance>
            get() = instancesRepo.computeIfAbsent(this){
                ArrayList()
            }
        fun AssemblyApplication.clearRegisterInstances(){
            instancesRepo[this] = ArrayList()
        }
        val instancesRepo = ConcurrentHashMap<AssemblyApplication, ArrayList<AssemblyInstance>>()
    }
    abstract val side: Side
    abstract val id:AssemblyIdentifier
    private val functions = ArrayList<Method>()

    init {
        Assembly.register {
            listener(this@AssemblyApplication)
        }

    }

    fun registerFunction(function: Method){
        functions.add(function)
    }

    override fun onReceive(sender: PacketSender, packet: AssemblyPacket) {
        packet.bind<FunctionInvoke> {
            if (this.appId == this@AssemblyApplication.id){
                val res = functions.find { it.name == this.name }?.invoke(null,*this.args!!.toTypedArray())


                sender.response(this.meta.transaction, AssemblyEntity.build<FunctionResponse> {
                    this.response = res
                    this.appId = this@AssemblyApplication.id
                })
            }
        }
        packet.bind<InstanceDelete> {
            if (appId == id){
                removeClientInstanceById(this@AssemblyApplication,instanceId)
            }
        }
        packet.bind<InstanceDeleteAll> {
            if (appId == id){
                removeAllClientInstance(this@AssemblyApplication,)
            }
        }
        packet.bind<InstanceAdd> {
            if (appId == id){
                if (Assembly.side == Side.CLIENT){
                    val obj = ClientInstanceManager.makeInstance(name,instanceId,attrs)
                    addClientInstance(this@AssemblyApplication,obj)
                }
            }
        }
        packet.bind<InstanceNow>{
            if (appId == id){
                if (Assembly.side == Side.CLIENT){
                    val obj = ClientInstanceManager.makeInstance(name,instanceId,attrs)
                    obj.mounted()
                }
            }
        }
    }
    fun requestFunction(sender: PacketSender,name: String, vararg args: Any): CompletableFuture<Any?> {
        val future = CompletableFuture<Any?>()
        AssemblyEntity.build<FunctionInvoke>{
            this.name = name
            this.args = ArrayList<Any>()
            this.appId = this@AssemblyApplication.id
            args.forEach {
                this.args!!.add(it)
            }

        }.sendWithResponse<FunctionResponse>(sender){
            future.complete(this.response)
        }
        return future
    }
}