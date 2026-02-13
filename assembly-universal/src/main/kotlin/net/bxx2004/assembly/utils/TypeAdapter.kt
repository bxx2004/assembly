package net.bxx2004.assembly.utils

import net.bxx2004.assembly.utils.BreakUtils.gson
import java.lang.reflect.Field

/**
 * @author 6hisea
 * @date  2026/1/26 15:18
 * @description: None
 */
object TypeAdapter {
    inline fun <reified Z>Map<*,*>.adapt():Z{
        return gson.fromJson(gson.toJson(this),Z::class.java)
    }
}

fun Class<*>.findAllFields(): List<Field> {
    val fields = mutableListOf<Field>()
    var currentClass: Class<*>? = this

    while (currentClass != null && currentClass != Any::class.java) {
        // 获取当前类声明的所有字段
        fields.addAll(currentClass.declaredFields)
        currentClass = currentClass.superclass
    }

    return fields
}