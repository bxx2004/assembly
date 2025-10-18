package net.bxx2004.script

class ThorOptions {
    var ENABLE_CACHE = true
    var CLASS_LOADER = Thread.currentThread().contextClassLoader
    companion object{
        @JvmStatic
        fun default():ThorOptions{
            return ThorOptions()
        }
    }
}