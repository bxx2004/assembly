package net.bxx2004.script.javascript

import net.bxx2004.script.simple.AbstractThorFunction
import net.bxx2004.script.simple.Parameter

class JSFunction(name: String, args: List<Parameter>, expression: String):AbstractThorFunction(name, args, expression){
    override fun compile(): String {
        var arg = ""
        args.forEach {
            arg += "${it.name}, "
        }
        if (arg.endsWith(", ")) arg = arg.substring(0,arg.length-2)
        return "function $name($arg)" +
                "{\n" +
                "    $expression\n" +
                "}"
    }
}