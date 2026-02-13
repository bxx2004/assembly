package net.bxx2004.assembly.data.attribute

/**
 * @author 6hisea
 * @date  2025/10/21 19:08
 * @description: None
 */
interface Attribute<T> {
    fun getValue():T
}

val Attribute<*>.asInt: Int
    get() = (this.getValue() as Number).toInt()
val Attribute<*>.asByte: Byte
    get() = (this.getValue() as Number).toByte()
val Attribute<*>.asShort: Short
    get() = (this.getValue() as Number).toShort()
val Attribute<*>.asLong: Long
    get() = (this.getValue() as Number).toLong()
val Attribute<*>.asFloat: Float
    get() = (this.getValue() as Number).toFloat()
val Attribute<*>.asDouble: Double
    get() = (this.getValue() as Number).toDouble()
val Attribute<*>.asBoolean: Boolean
    get() = this.getValue() as Boolean
val Attribute<*>.asString: String
    get() = this.getValue().toString()
val Attribute<*>.asChar: Char
    get() = this.getValue().toString().first()


private fun <T>literalAttribute(value: T): LiteralAttribute<T> {
    return object : LiteralAttribute<T>(value) {}
}
var <T>Attribute<T>.value:T
    get() = getValue()
    set(value) {
        if (this is LiteralAttribute<T>) {
            setValue(value)
        }
    }
fun <T>attribute(obj:T):Attribute<T> {
    return when (obj) {
        is Byte -> ByteAttribute(obj)
        is Short -> ShortAttribute(obj)
        is Int -> IntAttribute(obj)
        is Long -> LongAttribute(obj)
        is Float -> FloatAttribute(obj)
        is Double -> DoubleAttribute(obj)
        is String -> StringAttribute(obj)
        is Boolean -> BooleanAttribute(obj)
        is Char -> CharAttribute(obj)
        else -> literalAttribute(obj)
    } as Attribute<T>
}