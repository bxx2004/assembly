package net.bxx2004.script

import net.bxx2004.script.source.ThorSource

interface ThorShell {
    fun options():ThorOptions
    fun eval(source: ThorSource,variable:Map<String,Any?> = emptyMap(),executor:ThorExecutor = ThorExecutor.global()):Any?
    fun invokeFunction(compile: ThorCompile? = null,name:String,args:List<Any?> = emptyList(),variable:Map<String,Any?> = emptyMap()):Any?
    fun injectVariable(variable:Map<String,Any?> = emptyMap())
    fun injectFunction(function: ThorFunction,cache:Boolean=false)
    fun removeVariable(name:String)
    fun getVariable(name:String):Any?
    fun close()
    fun type():ThorType
    fun compile(source: ThorSource,variable:Map<String,Any?> = emptyMap()):ThorCompile
}