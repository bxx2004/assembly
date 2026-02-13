package net.bxx2004.assembly.utils

/**
 * @author 6hisea
 * @date  2026/2/11 15:39
 * @description: None
 */
private val prefix = "[assembly-logger]"
var logLevel = 1
fun warn(message:String){
    if (logLevel < 1) return
    println("$prefix[warn] $message")
}
fun log(message:String){
    println("$prefix[log] $message")
}
fun danger(message:String){
    if (logLevel < 2) return
    println("$prefix[danger] $message")
}
