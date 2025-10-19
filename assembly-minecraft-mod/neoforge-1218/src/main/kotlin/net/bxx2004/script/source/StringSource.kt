package net.bxx2004.script.source

import java.io.Reader

class StringSource(var s:String):ThorSource() {
    override fun reader(): Reader {
        return s.reader()
    }


    override fun toString(): String {
        return s
    }
}
fun source(s:String):ThorSource{
    return StringSource(s)
}