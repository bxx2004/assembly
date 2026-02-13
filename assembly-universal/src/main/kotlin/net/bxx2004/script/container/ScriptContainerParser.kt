package net.bxx2004.script.container

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.application.resource.ehc.parse
import net.bxx2004.assembly.data.Side
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.attribute
import net.bxx2004.script.container.ServerContainerHook.server

/**
 * @author 6hisea
 * @date  2026/2/12 14:45
 * @description: None
 */
object ScriptContainerParser : ElementParser<ScriptContainer>{
    override fun parse(
        key: String,
        value: ConfigValue
    ): Attribute<ScriptContainer> {
        val cf = value.atKey("temp").getConfig("temp")
        val sc = ScriptContainer()
        if (cf.hasPath("server")){
            cf.getConfig("server")?.let {
                it.entrySet().forEach { entry ->
                    sc.scriptMap.add(
                        ScriptUnit(
                            Side.SERVER,
                            entry.key,
                            entry.value.unwrapped().toString()
                        )
                    )
                }
            }
        }

        if (cf.hasPath("client")){
            cf.getConfig("client")?.let {
                it.entrySet().forEach { entry ->
                    sc.scriptMap.add(
                        ScriptUnit(
                            Side.CLIENT,
                            entry.key,
                            entry.value.unwrapped().toString()
                        )
                    )
                }
            }
        }
        if (cf.hasPath("common")){
            cf.getConfig("common")?.let {
                it.entrySet().forEach { entry ->
                    sc.scriptMap.add(
                        ScriptUnit(
                            Side.COMMON,
                            entry.key,
                            entry.value.unwrapped().toString()
                        )
                    )
                }
            }
        }

        sc.scriptMap.filter {
            it.side == Side.SERVER
        }.forEach {
            sc.server.add(it)
        }
        sc.scriptMap.removeIf {
            it.side == Side.SERVER
        }
        return attribute(sc)
    }
    const val ScriptContainerParser = "net.bxx2004.script.container.ScriptContainerParser"
}