package net.bxx2004.assembly.application.client

import net.bxx2004.assembly.Assembly
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2025/10/18 12:48
 * @description: None
 */
abstract class AssemblyInstance(val id: AssemblyIdentifier){
    fun mounted(){
        onMounted()
    }
    fun unmounted(){
        onUnmounted()
    }
    protected abstract fun onMounted()
    protected abstract fun onUnmounted()

    fun getAttrs():Map<String, Any?>{
        val hmap = hashMapOf<String, Any?>()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            val value = field.get(this)
            hmap[field.name] = value
        }
        return hmap
    }

    fun getResources(): List<AssemblyIdentifier>{
        val res = mutableListOf<AssemblyIdentifier>()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.type == AssemblyIdentifier::class.java){
                val value = field.get(this) as AssemblyIdentifier
                if (value.namespace == "resource"){
                    res.add(value)
                }
            }
        }
        return res
    }

    fun isReady(): Boolean {
        if (Assembly.side != Side.CLIENT) return true
        return getResources().all { ClientResourceManager.checkResource(it.path) }
    }
}