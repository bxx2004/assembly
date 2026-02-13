package net.bxx2004.assembly_neoforge.api

import net.bxx2004.assembly.AssemblyRegister
import net.neoforged.bus.api.Event


/**
 * @author 6hisea
 * @date  2025/10/29 10:36
 * @description: None
 */
class AssemblyRegisterEvent(val register: AssemblyRegister): Event() {
}