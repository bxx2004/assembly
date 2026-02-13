package net.bxx2004.assembly.data.attribute

import net.bxx2004.assembly.IShell
import net.bxx2004.script.ThorExecutor
import net.bxx2004.script.source.source

/**
 * @author 6hisea
 * @date  2025/10/22 12:20
 * @description: None
 */
class DynamicAttribute<T>(val script: String) : Attribute<T>{
    val vars = hashMapOf<String, Any?>()
    fun addVar(key:String, value:Any?){
        vars[key] = value
    }
    fun deleteVar(key:String){
        vars.remove(key)
    }
    override fun getValue(): T {
        return IShell.eval(source(script),vars, ThorExecutor.global()) as T
    }
    fun getValue(executor: ThorExecutor):T{
        return IShell.eval(source(script),vars, executor) as T
    }

    override fun toString(): String {
        return "dynamic(${getValue()})"
    }
}