package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.IShell
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.DynamicAttribute
import net.bxx2004.assembly.data.attribute.attribute
import net.bxx2004.script.source.source

/**
 * @author 6hisea
 * @date  2026/1/8 15:15
 * @description: None
 */
object JavaScriptParser : ElementParser<Any?> {
    override fun parse(key: String, value: ConfigValue): Attribute<Any?> {
        if (key.startsWith("~~")){
            return attribute(IShell.eval(source = source(value.unwrapped().toString())))
        }
        return DynamicAttribute(value.unwrapped().toString())
    }
}