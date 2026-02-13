package net.bxx2004.assembly

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.client.ClientInstanceManager
import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.application.entity.CustomRequest
import net.bxx2004.assembly.application.entity.ResourceShowMissing
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.core.tool.ToolCallingRequest
import net.bxx2004.assembly.core.tool.ToolCallingResponse
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.modules.JSON
import net.bxx2004.assembly.modules.Math
import net.bxx2004.assembly.modules.Requests
import net.bxx2004.assembly.network.controller.BreakDataManager
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.BreakAssemblyPacket
import net.bxx2004.assembly.network.packet.entity.AssemblyEntity
import net.bxx2004.script.ThorExecutor
import net.bxx2004.script.container.ScriptTool
import net.bxx2004.script.javascript.JSShell
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author 6hisea
 * @date  2025/10/2 17:06
 * @description: None
 */
val IShell = JSShell()

object Assembly {
    var MAX_PACKET_SIZE = 1048575L
    var CHANNEL = "assembly:network"
    var side: Side = Side.COMMON
    var DATA_DIR = ""

    private var receivers:PacketReceiver?=null
    private val tools = CopyOnWriteArrayList<Tool>()
    private val listeners = CopyOnWriteArrayList<PacketReceiver>()
    private val applications = CopyOnWriteArrayList<AssemblyApplication>()

    private var isInitialized = false

    fun registerApplication(application:AssemblyApplication) {
        applications.add(application)
    }
    fun getApplications(): List<AssemblyApplication> {
        return applications
    }

    fun removeAllApplications() {
        if (side == Side.CLIENT) {
            applications.forEach {
                ClientInstanceManager.removeAllClientInstance(it)
            }
        }
        applications.clear()
    }
    fun init(a: Side, func:(PacketSender, AssemblyPacket)->Unit) {
        if (isInitialized) return
        isInitialized = true
        side = a

        if (side == Side.CLIENT) {
            listeners.add(ClientResourceManager)
            listeners.add(object : PacketReceiver {
                override fun onReceive(sender: PacketSender, packet: AssemblyPacket) {
                    packet.bind<ResourceShowMissing> {
                        ClientResourceManager.showMissingResources()
                    }
                }
            })
        }
        tools.add(ScriptTool)
        registerTools()
        registerModules()
        receivers = object : PacketReceiver {
            override fun onReceive(sender: PacketSender,packet: AssemblyPacket) {
                if (packet is BreakAssemblyPacket) {
                    BreakDataManager.put(sender, packet)
                }else{
                    func(sender,packet)
                }
            }
        }
        printLogo()
    }

    fun printLogo(){
        val logo = """
[assembly]     _                                     _       _         
[assembly]    / \     ___   ___    ___   _ __ ___   | |__   | |  _   _ 
[assembly]   / _ \   / __| / __|  / _ \ | '_ ` _ \  | '_ \  | | | | | |
[assembly]  / ___ \  \__ \ \__ \ |  __/ | | | | | | | |_) | | | | |_| |
[assembly] /_/   \_\ |___/ |___/  \___| |_| |_| |_| |_.__/  |_|  \__, |
[assembly]                                                       |___/ 
        """.trimIndent()
        val info = """
[assembly] Ver 2.0.0 By bxx2004. Current Environment: ${side}
[assembly] Github: https://github.com/bxx2004 | Welcome!
        """.trimIndent()
        println(logo + "\n" + info)
    }

    fun registerTools(){
        listeners.add(object : PacketReceiver {
            override fun onReceive(sender: PacketSender, packet: AssemblyPacket) {
                packet.bind<ToolCallingRequest> {
                    val res = tools.find { it.name == toolName }?.invoke(sender,methodName!!,data)
                    sender.response(packet.meta.transaction,
                        AssemblyEntity.build<ToolCallingResponse> {
                            this.result = res
                        })
                }
            }
        })
    }
    fun register(func:AssemblyRegister.()->Unit){
        val reg = AssemblyRegister()
        func(reg)

        if (reg.customRequestHandler.isNotEmpty()){
            listeners.add(object : PacketReceiver {
                override fun onReceive(sender: PacketSender, packet: AssemblyPacket) {
                    packet.bind<CustomRequest> {
                        reg.customRequestHandler.forEach {
                            it.onReceive(sender,this)
                        }
                    }
                }
            })
        }
        if (reg.tools.isNotEmpty()){
            tools.addAll(reg.tools)
        }
    }
    @Deprecated("use register")
    fun addListener(receiver: PacketReceiver) {
        listeners.add(receiver)
    }

    fun listenNextTransaction(transaction: UUID,timeout:Long=-1,timeoutFunc:()->Unit = {},func: (PacketSender, AssemblyPacket) -> Unit){
        val lis = object : PacketReceiver {
            override fun onReceive(sender: PacketSender,packet: AssemblyPacket) {
                if (packet.meta.transaction == transaction){
                    func(sender,packet)
                    listeners.remove(this)
                }
            }
        }
        listeners.add(lis)
        if (timeout != -1L){
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    listeners.remove(lis)
                    timeoutFunc()
                }
            }, timeout)
        }
    }

    fun callReceivePacket(sender: PacketSender,receiver: AssemblyPacket) {
        receivers?.onReceive(sender, receiver)
        listeners.forEach { it.onReceive(sender, receiver) }
    }
    private fun registerModules(){
        Math
        JSON
        Requests
    }
}