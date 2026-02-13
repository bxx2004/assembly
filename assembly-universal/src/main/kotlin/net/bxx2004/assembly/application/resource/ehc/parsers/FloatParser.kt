package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.FloatAttribute

/**
 * @author 6hisea
 * @date  2026/2/10 21:10
 * @description: None
 */
object FloatParser : ElementParser<Float> {
    override fun parse(key: String, value: ConfigValue): Attribute<Float> {
        return FloatAttribute(value.unwrapped().toString().toFloat())
    }
}