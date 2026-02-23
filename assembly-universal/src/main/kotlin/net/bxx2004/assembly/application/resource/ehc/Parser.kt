package net.bxx2004.assembly.application.resource.ehc

import kotlin.reflect.KClass

/**
 * @author 6hisea
 * @date  2026/2/12 11:45
 * @description: None
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Parser(
    val cls: String,
    val name: String = "",
    val wrapper:Boolean = true,
)

private const val innerName = "net.bxx2004.assembly.application.resource.ehc.parsers."
const val AssemblyIdentifierParser = "${innerName}AssemblyIdentifierParser"
const val ResourceParser = "${innerName}ResourceParser"

const val BooleanParser = "${innerName}BooleanParser"
const val DoubleParser = "${innerName}DoubleParser"
const val FloatParser = "${innerName}FloatParser"
const val IntParser = "${innerName}IntParser"
const val JavaScriptParser = "${innerName}JavaScriptParser"
const val LongParser = "${innerName}LongParser"
const val StringParser = "${innerName}StringParser"