package net.bxx2004.script.simple

import net.bxx2004.script.ThorFunction

abstract class AbstractThorFunction(val name:String, val args:List<Parameter>, val expression:String): ThorFunction {
    override fun name(): String {
        return name
    }

    override fun args(): List<Parameter> {
        return args
    }

    override fun expression(): String {
        return expression
    }
}