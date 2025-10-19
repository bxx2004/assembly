package net.bxx2004.script

import java.io.Reader

interface ThorCompile {
    fun adapt():Any
    fun source():Reader
    fun eval(variable:Map<String,Any?> = emptyMap()):Any?
    fun invokeFunction(name:String,args:List<Any?> = emptyList()):Any?
    fun variable():Map<String,Any?>
    fun injectVariable(variables:Map<String,Any?>)
}