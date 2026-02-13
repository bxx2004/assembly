package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.DoubleAttribute

/**
 * @author 6hisea
 * @date  2026/1/8 15:15
 * @description: None
 */
object DoubleParser : ElementParser<Double> {
    override fun parse(key: String, value: ConfigValue): Attribute<Double> {
        return DoubleAttribute(value.unwrapped().toString().toDouble())
    }
}