package net.bxx2004.assembly_bukkit.modules

import net.bxx2004.script.module.IModule

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

@Awake(LifeCycle.INIT)
object KetherScript : IModule() {
    override val isInject: Boolean = true
    override fun name(): Array<String> {
        return arrayOf("Kether", "kether", "KetherScript", "ketherscript", "ks", "kether-script", "kether_script")
    }



    fun eval(sender: ProxyCommandSender, vars: HashMap<String, Any?>, script: String): Any? {
        return KetherShell.eval(script, ScriptOptions.new {
            sender(sender)
            vars(vars)
        }).get()
    }

    fun eval(sender: ProxyCommandSender, script: String): Any? {

        return KetherShell.eval(script, ScriptOptions.new {
            sender(sender)
        }).get()
    }

    fun eval(sender: CommandSender, vars: HashMap<String, Any?>, script: String): Any? {
        return KetherShell.eval(script, ScriptOptions.new {
            sender(adaptCommandSender(sender))
            vars(vars)
        }).get()
    }

    fun eval(sender: CommandSender, script: String): Any? {
        return KetherShell.eval(script, ScriptOptions.new {
            sender(adaptCommandSender(sender))
        }).get()
    }
    fun eval(script: String): Any? {
        return KetherShell.eval(script, ScriptOptions.new {
            sender(adaptCommandSender(Bukkit.getConsoleSender()))
        }).get()
    }
}