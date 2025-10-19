package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.data.AssemblyIdentifier

/**
 * @author 6hisea
 * @date  2025/10/18 16:45
 * @description: None
 */
object ClientInstanceManager{

    private val instanceFactory = HashMap<String, Class<AssemblyInstance>>()
    fun registerFactory(instance: Class<AssemblyInstance>) {
        instanceFactory[instance.simpleName] = instance
    }
    fun makeInstance(name: String,id: AssemblyIdentifier,attrs: Map<String,Any?>): AssemblyInstance {
        val obj = instanceFactory[name]?.getDeclaredConstructor(AssemblyApplication::class.java)?.newInstance(id)
        if (obj == null) {
            throw RuntimeException("Failed to create instance for $name")
        }
        obj::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            try {
                field.set(obj, attrs[field.name])
            }catch (e:Exception){
                throw RuntimeException("Failed to set instance for $name:${field.name}")
            }
        }
        return obj
    }
    fun addClientInstance(app:AssemblyApplication,ins:AssemblyInstance) {
        if (app.instances.map { it.id.path }.contains(ins.id.path)){
            return
        }

        (app.instances as ArrayList<AssemblyInstance>).add(ins)
        ins.mounted()
    }
    fun removeClientInstance(app:AssemblyApplication,inst:AssemblyInstance) {
        inst.unmounted()
        (app.instances as ArrayList<AssemblyInstance>).remove(inst)
    }
    fun removeClientInstanceById(app:AssemblyApplication,id: AssemblyIdentifier) {
        app.instances.find { it.id == id }?.unmounted()
        (app.instances as ArrayList<AssemblyInstance>).removeIf { it.id == id }
    }
    fun removeAllClientInstance(app:AssemblyApplication) {
        app.instances.forEach { it.unmounted() }
        (app.instances as ArrayList<AssemblyInstance>).clear()
    }
}