package net.bxx2004.assembly

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.modules.Math
import net.bxx2004.assembly.network.controller.BreakDataManager
import net.bxx2004.assembly.network.controller.PacketReceiver
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.network.packet.AssemblyPacket
import net.bxx2004.assembly.network.packet.BreakAssemblyPacket
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author 6hisea
 * @date  2025/10/2 17:06
 * @description: None
 */
object Assembly {
    var MAX_PACKET_SIZE = 1048575L
    var CHANNEL = "assembly:network"
    private var receivers:PacketReceiver?=null
    private val listeners = CopyOnWriteArrayList<PacketReceiver>()
    var side: Side = Side.COMMON
    var DATA_DIR = ""
    private val applications = CopyOnWriteArrayList<AssemblyApplication>()

    fun registerApplication(application:AssemblyApplication) {
        applications.add(application)
    }
    fun getApplications(): List<AssemblyApplication> {
        return applications
    }

    fun register(a: Side, func:(PacketSender, AssemblyPacket)->Unit) {
        side = a
        if (side == Side.CLIENT) {
            listeners.add(ClientResourceManager)
        }
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
    }

    fun addListener(receiver: PacketReceiver) {
        listeners.add(receiver)
    }

    fun listenNextTransaction(transaction: UUID,func: (PacketSender, AssemblyPacket) -> Unit){
        listeners.add(object : PacketReceiver {
            override fun onReceive(sender: PacketSender,packet: AssemblyPacket) {
                if (packet.meta.transaction == transaction){
                    func(sender,packet)
                    listeners.remove(this)
                }
            }
        })
    }

    fun callReceivePacket(sender: PacketSender,receiver: AssemblyPacket) {
        receivers?.onReceive(sender, receiver)
        listeners.forEach { it.onReceive(sender, receiver) }
    }
    fun registerModules(){
        Math
    }
}