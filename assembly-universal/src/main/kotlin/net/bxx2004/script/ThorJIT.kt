package net.bxx2004.script

import net.bxx2004.script.source.ThorSource


interface ThorJIT {
    fun compile(type:String,source:ThorSource,shell: ThorShell):ThorCompile
}