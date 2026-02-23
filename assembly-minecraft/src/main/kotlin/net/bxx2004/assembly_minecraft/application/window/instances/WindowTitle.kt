package net.bxx2004.assembly_minecraft.application.window.instances

import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.ehc.AssemblyIdentifierParser
import net.bxx2004.assembly.application.resource.ehc.Parser
import net.bxx2004.assembly.application.resource.ehc.StringParser
import net.bxx2004.assembly.data.AssemblyIdentifier

/**
 * @author 6hisea
 * @date  2026/2/23 17:04
 * @description: None
 */
open class WindowTitle : AssemblyInstance(){
    @Parser(AssemblyIdentifierParser, wrapper = false)
    override var id: AssemblyIdentifier = AssemblyIdentifier.random()
    @Parser(StringParser, wrapper = false)
    var title: String = "Minecraft Client | This title is modified by Assembly."
}