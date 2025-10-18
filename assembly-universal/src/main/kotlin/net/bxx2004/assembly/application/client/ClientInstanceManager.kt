package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.data.AssemblyIdentifier
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 6hisea
 * @date  2025/10/18 16:45
 * @description: None
 */
object ClientInstanceManager {
    private val instancesRepo = ConcurrentHashMap<AssemblyApplication, ArrayList<AssemblyInstance>>()

    val AssemblyApplication.instances: List<AssemblyInstance>
        get() = instancesRepo.computeIfAbsent(this){
            ArrayList()
        }
    fun AssemblyApplication.addInstance(ins:AssemblyInstance) {
        if (instances.map { it.id.path }.contains(ins.id.path)){
            return
        }
        (instances as ArrayList<AssemblyInstance>).add(ins)
    }
    fun AssemblyApplication.removeInstance(inst:AssemblyInstance) {
        (instances as ArrayList<AssemblyInstance>).remove(inst)
    }
    fun AssemblyApplication.removeInstanceById(id: AssemblyIdentifier) {
        (instances as ArrayList<AssemblyInstance>).removeIf { it.id == id }
    }

}