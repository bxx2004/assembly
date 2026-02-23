package net.bxx2004.assembly_bukkit.modules

import net.bxx2004.script.module.IModule
import org.bukkit.event.Event
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * @author 6hisea
 * @date  2025/9/25 17:17
 * @description: None
 */
@Awake(LifeCycle.INIT)
object EventModule : IModule(){
    override val isInject: Boolean = true
    override fun name(): Array<String> {
        return arrayOf("BukkitEvent","event")
    }
    fun equal(event: Event,clazz: String): Boolean {
        return event.eventName == clazz
    }
    fun equal(clazz: String,event: Event): Boolean {
        return event.eventName == clazz
    }
}