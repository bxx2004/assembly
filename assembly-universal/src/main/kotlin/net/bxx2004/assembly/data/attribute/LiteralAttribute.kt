package net.bxx2004.assembly.data.attribute

/**
 * @author 6hisea
 * @date  2025/10/21 19:24
 * @description: None
 */
abstract class LiteralAttribute<T>(private var value:T) : Attribute<T> {
    open fun setValue(value:T){
        this.value = value
    }
    override fun getValue(): T {
        return value
    }
    override fun toString(): String {
        return "${this::class.java.simpleName}(${getValue()})"
    }
}