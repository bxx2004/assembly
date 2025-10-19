package net.bxx2004.script.simple

data class Parameter(val name:String,val type:String?=null) {
    companion object{
        @JvmStatic
        fun single(name:String):Parameter{
            return Parameter(name)
        }
    }
}