package net.bxx2004.assembly_bukkit.modules

import net.bxx2004.script.module.IModule
import org.bukkit.entity.Player
import org.bukkit.event.Event

import org.bukkit.plugin.Plugin
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.expansion.dispatchCommandAsOp
import taboolib.platform.util.bukkitPlugin
import kotlin.collections.set

@Awake
object BukkitUtil : IModule(){
    override fun name(): Array<String> {
        return arrayOf("bukkit-util")
    }
    fun commandAsAdmin(player: Player,command:String){
        player.dispatchCommandAsOp(command)
    }
    fun command(player: Player,command:String){
        player.performCommand(command)
    }
    fun plugin():Plugin{
        return bukkitPlugin
    }
    fun registerCommand(name: String, permission: String, function: (sender: ProxyCommandSender,args:Array<String>)->Unit) {
        simpleCommand(name, permission = permission) { sender, args ->
            function(sender,args)
        }
    }

    fun unregisterCommand(name: String) {
        taboolib.common.platform.function.unregisterCommand(name)
    }
    private val listeners = HashMap<String, ProxyListener>()
    fun registerListener(id:String,clazz: String, function: (e:Event) -> Unit) {
        listeners[id] = registerBukkitListener(Class.forName(clazz), EventPriority.NORMAL,true){
            function(it as Event)
        }
    }

    fun unregisterListener(id:String) {
        listeners.get(id)?.let {
            taboolib.common.platform.function.unregisterListener(it)
        }
        listeners.remove(id)
    }
}