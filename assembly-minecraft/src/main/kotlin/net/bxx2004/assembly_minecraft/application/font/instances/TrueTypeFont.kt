package net.bxx2004.assembly_minecraft.application.font.instances

import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.ehc.*
import net.bxx2004.assembly.application.resource.ehc.Parser
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id

/**
 * @author 6hisea
 * @date  2026/2/10 18:22
 * @description: None
 */
open class TrueTypeFont : AssemblyInstance() {
    @Parser(AssemblyIdentifierParser, wrapper = false)
    override var id: AssemblyIdentifier = AssemblyIdentifier.random()
    @Parser(ResourceParser, wrapper = false)
    var reference = "ref:font.ttf".id()
    @Parser(BooleanParser, wrapper = false)
    var global = false
    @Parser(FloatParser, wrapper = false)
    var size: Float = 8.0F
    @Parser(FloatParser, wrapper = false)
    var oversample: Float = 10.0F
    @Parser(FloatParser, wrapper = false)
    var shiftX: Float = 0.0F
    @Parser(FloatParser, wrapper = false)
    var shiftY: Float = 0.0F
    @Parser(StringParser, wrapper = false)
    var excludedCharacters: String = ""
}