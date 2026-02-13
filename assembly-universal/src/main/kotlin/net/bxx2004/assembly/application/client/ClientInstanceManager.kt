package net.bxx2004.assembly.application.client

import com.google.gson.internal.LinkedTreeMap
import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.application.AssemblyInstance
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.utils.BreakUtils.gson
import net.bxx2004.assembly.utils.findAllFields

/**
 * @author 6hisea
 * @date  2025/10/18 16:45
 * @description: None
 */
object ClientInstanceManager{

    private val instanceFactory = HashMap<String, Class<out AssemblyInstance>>()
    fun registerFactory(instance: Class<out AssemblyInstance>) {
        if (instance.annotations.filterIsInstance<Redirect>().isNotEmpty()) {
            val id = instance.annotations.filterIsInstance<Redirect>().first()
            if (id.name == "superclass"){
                instanceFactory[instance.superclass.simpleName] = instance
            }else{
                instanceFactory[id.name] = instance
            }

        }else{
            instanceFactory[instance.simpleName] = instance
        }

    }
    fun makeInstance(name: String,id: AssemblyIdentifier,attrs: Map<String,Any?>): AssemblyInstance {
        val obj = if (instanceFactory[name]?.declaredFields!!.map { it.name }.contains("INSTANCE")) {
            instanceFactory[name]?.getDeclaredField("INSTANCE")?.get(null) as AssemblyInstance
        }else{
            var target = try {
                instanceFactory[name]?.getDeclaredConstructor(AssemblyIdentifier::class.java)?.newInstance(id)
            }catch (e: NoSuchMethodException){
                instanceFactory[name]?.getDeclaredConstructor()?.newInstance().apply {
                    this!!.id = id
                }
            }
            target
        }
        if (obj == null) {
            throw RuntimeException("Failed to create instance for $name")
        }
        obj::class.java.findAllFields().forEach { field ->


            field.isAccessible = true
            if (field.name != "INSTANCE") {
                try {
                    when (field.type) {
                        Byte::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toByte())
                        }
                        Byte::class.javaPrimitiveType -> {
                            field.setByte(obj, (attrs[field.name] as Number).toByte())
                        }
                        Short::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toShort())
                        }
                        Short::class.javaPrimitiveType -> {
                            field.setShort(obj, (attrs[field.name] as Number).toShort())
                        }
                        Int::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toInt())
                        }
                        Int::class.javaPrimitiveType -> {
                            field.setInt(obj, (attrs[field.name] as Number).toInt())
                        }
                        Long::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toLong())
                        }
                        Long::class.javaPrimitiveType -> {
                            field.setLong(obj, (attrs[field.name] as Number).toLong())
                        }
                        Float::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toFloat())
                        }
                        Float::class.javaPrimitiveType -> {
                            field.setFloat(obj, (attrs[field.name] as Number).toFloat())
                        }
                        Double::class.java -> {
                            field.set(obj, (attrs[field.name] as Number).toDouble())
                        }
                        Double::class.javaPrimitiveType -> {
                            field.setDouble(obj, (attrs[field.name] as Number).toDouble())
                        }
                        Boolean::class.java -> {
                            field.set(obj, attrs[field.name].toString().toBoolean())
                        }
                        Boolean::class.javaPrimitiveType -> {
                            field.setBoolean(obj, attrs[field.name].toString().toBoolean())
                        }
                        Char::class.java -> {
                            field.set(obj, attrs[field.name].toString()[0])
                        }
                        Char::class.javaPrimitiveType -> {
                            field.setChar(obj, attrs[field.name].toString()[0])
                        }
                        String::class.java -> {
                            field.set(obj, attrs[field.name].toString())
                        }
                        else -> {
                            val value = attrs[field.name]
                            if (value != null){
                                if (value::class.java == LinkedTreeMap::class.java) {
                                    field.set(obj, gson.fromJson(gson.toJson(value),field.type))
                                }else{
                                    field.set(obj,value)
                                }
                            }

                        }
                    }
                }catch (e:Exception){
                    throw RuntimeException("Failed to set instance for $name:${field.name}. ${e.message}")
                }
            }
        }
        return obj
    }
    fun addClientInstance(app:AssemblyApplication,ins: AssemblyInstance) {
        if (app.instances.map { it.id }.contains(ins.id)){
            return
        }

        (app.instances as ArrayList<AssemblyInstance>).add(ins)
        ins.mounted()
    }
    fun removeClientInstance(app:AssemblyApplication,inst: AssemblyInstance) {
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