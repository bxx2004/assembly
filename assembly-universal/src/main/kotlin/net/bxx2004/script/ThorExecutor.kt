package net.bxx2004.script

interface ThorExecutor {
    fun tell(message:String)
    fun adapt():Any
    fun name():String
    fun platform():String
    companion object{
        @JvmStatic
        fun global():ThorExecutor{
            return object :ThorExecutor{
                override fun tell(message: String) {
                    println("[Thor] $message")
                }

                override fun adapt(): Any {
                    return this
                }

                override fun name(): String {
                    return "Thor"
                }

                override fun platform(): String {
                    return "System"
                }

            }
        }
    }
}