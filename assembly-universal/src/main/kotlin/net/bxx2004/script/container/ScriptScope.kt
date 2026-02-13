package net.bxx2004.script.container

import net.bxx2004.assembly.network.controller.PacketSender

/**
 * @author 6hisea
 * @date  2026/2/12 20:36
 * @description: None
 */
class ScriptScope(
    val sender: PacketSender,
    private val sc:ScriptContainer,
) {
    fun client(key: String,vars: Map<String, Any?> = mapOf()): Any?{
        return sc.evalClient(sender,key,vars)
    }
    fun server(key: String,vars: Map<String, Any?> = mapOf()): Any?{
        return sc.evalServer(sender,key,vars)
    }
}