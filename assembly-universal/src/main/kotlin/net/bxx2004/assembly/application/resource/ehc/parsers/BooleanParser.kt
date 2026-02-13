package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.BooleanAttribute

/**
 * @author 6hisea
 * @date  2026/1/8 15:05
 * @description: None
 */
object BooleanParser : ElementParser<Boolean> {
    override fun parse(key: String, value: ConfigValue): Attribute<Boolean> {
        return BooleanAttribute(value.unwrapped().toString().toBoolean())
    }
}