package net.bxx2004.assembly.application

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.application.client.ClientResourceManager
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2025/10/18 12:48
 * @description: None
 */
abstract class AssemblyInstance{
    abstract var id: AssemblyIdentifier
    var state = false
    open fun mounted(){}
    open fun unmounted(){}

    fun getAttrs():Map<String, Any?>{
        val hmap = hashMapOf<String, Any?>()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.name != "INSTANCE") {
                val value = field.get(this)
                hmap[field.name] = value
            }
        }
        return hmap
    }

    fun getResources(): List<AssemblyIdentifier>{
        val res = mutableListOf<AssemblyIdentifier>()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.type == AssemblyIdentifier::class.java){
                val value = field.get(this) as AssemblyIdentifier
                if (value.namespace == "ref"){
                    res.add(value)
                }
            }
        }
        return res
    }

    protected fun pushMissResources(missingResources: List<String> = arrayListOf<String>()){
        val missing = mutableSetOf<String>()
        for (path in getResources().map { it.path }) {
            if (!ClientResourceManager.checkResource(path)){
                missing.add(path)
            }
        }
        missing.addAll(missingResources)
        ClientResourceManager.missingResources.addAll(missing)
    }

    fun isReady(): Pair<Boolean,List<String>> {
        if (Assembly.side != Side.CLIENT) return Pair(false,listOf())
        val missing = arrayListOf<String>()
        for (path in getResources().map { it.path }) {
            if (!ClientResourceManager.checkResource(path)){
                missing.add(path)
                state = false
            }
        }
        pushMissResources()
        return Pair(state,missing)
    }
}