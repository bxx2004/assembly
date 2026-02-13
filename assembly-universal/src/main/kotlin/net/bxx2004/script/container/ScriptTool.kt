package net.bxx2004.script.container

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.core.tool.DataReader
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.utils.BreakUtils.gson
import net.bxx2004.assembly.utils.property
/**
 * @author 6hisea
 * @date  2026/2/12 15:50
 * @description: None
 */
object ScriptTool : Tool() {
    override val provider: String
        get() = "assembly-script"
    override val version: String
        get() = "lastest"
    override val name: String
        get() = "script"
    fun evalServer(sender: PacketSender,reader: DataReader):Any?{
        val appId = reader.read<AssemblyIdentifier>("appId")
        val insId = reader.read<AssemblyIdentifier>("instanceId")
        val name = reader.read<String>("name")
        val data = reader.read<Map<String,Any?>>("data")
        return Assembly.getApplications().find {
            it.id == appId
        }?.instances?.find {
            it.id == insId
        }?.property<ScriptContainer>("scriptContainer")?.evalServer(
            sender,name!!,data!!
        )
    }




    fun evalClient(sender: PacketSender,reader: DataReader):Any?{
        val appId = reader.read<AssemblyIdentifier>("appId")
        val insId = reader.read<AssemblyIdentifier>("instanceId")
        val name = reader.read<String>("name")
        val data = reader.read<Map<String,Any?>>("data")
        return Assembly.getApplications().find {
            it.id == appId
        }?.instances?.find {
            it.id == insId
        }?.property<ScriptContainer>("scriptContainer")?.evalClient(
            sender,name!!,data?:mapOf()
        )
    }
}