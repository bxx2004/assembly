package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.IntAttribute

/**
 * @author 6hisea
 * @date  2026/1/8 15:13
 * @description: None
 */
object IntParser : ElementParser<Int> {
    override fun parse(key: String, value: ConfigValue): Attribute<Int> {
        return IntAttribute(value.unwrapped().toString().toInt())
    }
}