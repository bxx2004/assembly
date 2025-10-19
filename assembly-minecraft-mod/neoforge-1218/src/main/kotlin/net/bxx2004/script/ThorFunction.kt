package net.bxx2004.script

import net.bxx2004.script.simple.Parameter

interface ThorFunction {
    fun name():String
    fun args():List<Parameter>
    fun expression():String
    fun compile():String
}