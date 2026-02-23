package net.bxx2004.assembly_minecraft.application.window.instances

import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.application.resource.ehc.AssemblyIdentifierParser
import net.bxx2004.assembly.application.resource.ehc.Parser
import net.bxx2004.assembly.application.resource.ehc.ResourceParser
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.AssemblyIdentifier.Companion.id

/**
 * @author 6hisea
 * @date  2026/2/23 17:10
 * @description: None
 */
open class WindowIcon : AssemblyInstance(){
    @Parser(AssemblyIdentifierParser, wrapper = false)
    override var id: AssemblyIdentifier = AssemblyIdentifier.random()
    @Parser(ResourceParser, wrapper = false)
    var icon: AssemblyIdentifier = "ref:icon.png".id()
}