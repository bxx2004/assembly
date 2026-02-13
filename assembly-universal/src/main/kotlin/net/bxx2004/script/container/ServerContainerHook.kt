package net.bxx2004.script.container

import java.util.concurrent.ConcurrentHashMap

/**
 * @author 6hisea
 * @date  2026/2/12 15:43
 * @description: None
 */
object ServerContainerHook {
    private val serverContainer = ConcurrentHashMap<ScriptContainer, ArrayList<ScriptUnit>>()
    val ScriptContainer.server: ArrayList<ScriptUnit>
        get() = serverContainer.computeIfAbsent(this) { ArrayList() }
}