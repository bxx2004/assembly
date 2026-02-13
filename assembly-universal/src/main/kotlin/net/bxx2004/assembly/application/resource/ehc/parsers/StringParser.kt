package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.StringAttribute

/**
 * @author 6hisea
 * @date  2026/1/8 15:05
 * @description: None
 */
object StringParser : ElementParser<String> {
    override fun parse(key: String, value: ConfigValue): Attribute<String> {
        return StringAttribute(value.unwrapped().toString())
    }
}