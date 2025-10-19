package net.bxx2004.script.source

import java.io.File
import java.io.Reader

class FileSource(val file:File):ThorSource() {
    override fun reader(): Reader {
        return file.reader()
    }
}
fun source(file: File):ThorSource{
    return FileSource(file)
}