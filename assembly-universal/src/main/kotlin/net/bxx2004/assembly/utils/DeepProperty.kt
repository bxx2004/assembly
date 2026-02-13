package net.bxx2004.assembly.utils

/**
 * @author 6hisea
 * @date  2026/2/12 15:58
 * @description: None
 */
fun <T>Any.property(name:String): T? {
    return this::class.java.getDeclaredField(name).apply {
        isAccessible = true
    }.get(this) as T?
}
fun <T>Any.property(name:String,value:T?): T? {
    return this::class.java.getDeclaredField(name).apply {
        isAccessible = true
    }.set(this,value) as T?
}