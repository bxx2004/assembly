package net.bxx2004.script.container

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.IShell
import net.bxx2004.assembly.core.tool.Tool
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.network.controller.PacketSender
import net.bxx2004.assembly.utils.danger
import net.bxx2004.script.ThorExecutor
import net.bxx2004.script.container.ServerContainerHook.server
import net.bxx2004.script.source.source
import java.util.concurrent.TimeUnit
import kotlin.collections.mapOf

/**
 * @author 6hisea
 * @date  2026/2/12 14:22
 * @description: None
 */
class ScriptContainer() {
    private lateinit var appId: AssemblyIdentifier
    private lateinit var insId: AssemblyIdentifier

    val scriptMap = ArrayList<ScriptUnit>()
   fun evalClient(sender: PacketSender,key: String,vars: Map<String, Any?> = mapOf()):Any?{
        if (Assembly.side == Side.CLIENT) {
            return scriptMap.find { it.name == key }?.apply {
                IShell.eval(source(script.trimIndent()),vars + mapOf("scope" to ScriptScope(sender,this@ScriptContainer)),sender as ThorExecutor)
            }
        }
        return try {
            Tool.delegate(sender,"script","evalClient",
                mapOf(
                    "appId" to appId,
                    "instanceId" to insId,
                    "name" to key,
                    "data" to vars
                )).get(60, TimeUnit.SECONDS)
        }catch (e: Exception){
            danger("调用服务端函数失败: application:${appId},instance:${insId},name:${key}")
            null
        }
    }
    fun evalServer(sender: PacketSender, key: String, vars: Map<String, Any?> = mapOf()):Any?{
        if (Assembly.side == Side.SERVER) {
            return server.find { it.name == key }?.apply {
                IShell.eval(source(script.trimIndent()),vars + mapOf("scope" to ScriptScope(sender,this@ScriptContainer)),sender as ThorExecutor)
            }
        }
        return try {
            Tool.delegate(sender,"script","evalServer",
                mapOf(
                    "appId" to appId,
                    "instanceId" to insId,
                    "name" to key,
                    "data" to vars
                )).get(60, TimeUnit.SECONDS)
        }catch (e: Exception){
            e.printStackTrace()
            danger("调用服务端函数失败: application:${appId},instance:${insId},name:${key}")
            null
        }
    }

}