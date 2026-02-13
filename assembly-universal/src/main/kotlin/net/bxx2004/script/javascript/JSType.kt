package net.bxx2004.script.javascript

import net.bxx2004.script.ThorType

object JSType : ThorType {
    override fun name(): String {
        return "Nashorn JavaScript"
    }

    override fun version(): String {
        return "Nashorn 15.4"
    }
}