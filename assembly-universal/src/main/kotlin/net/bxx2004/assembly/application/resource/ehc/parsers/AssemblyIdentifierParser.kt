package net.bxx2004.assembly.application.resource.ehc.parsers

import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.ElementParser
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.attribute

/**
 * @author 6hisea
 * @date  2026/2/10 21:04
 * @description: None
 */
object AssemblyIdentifierParser: ElementParser<AssemblyIdentifier> {
    override fun parse(key: String, value: ConfigValue): Attribute<AssemblyIdentifier> {
        return attribute(value.unwrapped().toString().id())
    }
}