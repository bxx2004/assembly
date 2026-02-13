package net.bxx2004.assembly_minecraft.application.key.instances

import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.ehc.AssemblyIdentifierParser
import net.bxx2004.assembly.application.resource.ehc.BooleanParser
import net.bxx2004.assembly.application.resource.ehc.IntParser
import net.bxx2004.assembly.application.resource.ehc.Parser
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.script.container.ScriptContainer
import net.bxx2004.script.container.ScriptContainerParser
import net.bxx2004.script.container.ScriptContainerProvider

/**
 * @author 6hisea
 * @date  2026/2/12 14:18
 * @description: None
 */
open class SimpleKey : AssemblyInstance(), ScriptContainerProvider{
    @Parser(ScriptContainerParser.ScriptContainerParser, wrapper = false, name = "script")
    override var scriptContainer = ScriptContainer()
    @Parser(AssemblyIdentifierParser, wrapper = false)
    override var id: AssemblyIdentifier = AssemblyIdentifier.random()
    @Parser(IntParser,wrapper = false)
    var key = 72
    @Parser(BooleanParser,wrapper = false)
    var screenable = false
}