package net.bxx2004.assembly.modules

import net.bxx2004.script.module.IModule
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random


object Math : IModule() {
    override val isInject: Boolean = true

    override fun name(): Array<String> {
        return arrayOf("Math","math")
    }

    fun max(a: Number, b: Number): Number {
        return kotlin.math.max(a.toDouble(), b.toDouble())
    }

    fun min(a: Number, b: Number): Number {
        return kotlin.math.min(a.toDouble(), b.toDouble())
    }

    fun abs(value: Number): Number {
        return kotlin.math.abs(value.toDouble())
    }

    fun sqrt(value: Number): Number {
        return kotlin.math.sqrt(value.toDouble())
    }

    fun pow(base: Number, exponent: Number): Number {
        return base.toDouble().pow(exponent.toDouble())
    }

    fun round(value: Number): Number {
        return kotlin.math.round(value.toDouble())
    }
    fun round(value: Number,x:Int): Number {
        return value.toString().format("%.${x}f").toDouble()
    }

    fun floor(value: Number): Number {
        return kotlin.math.floor(value.toDouble())
    }

    fun ceil(value: Number): Number {
        return kotlin.math.ceil(value.toDouble())
    }

    fun sin(angle: Number): Number {
        return kotlin.math.sin(angle.toDouble())
    }

    fun cos(angle: Number): Number {
        return kotlin.math.cos(angle.toDouble())
    }

    fun tan(angle: Number): Number {
        return kotlin.math.tan(angle.toDouble())
    }

    fun log(value: Number): Number {
        return ln(value.toDouble())
    }

    fun log10(value: Number): Number {
        return kotlin.math.log10(value.toDouble())
    }

    fun randomDouble(): Number {
        return Random.nextDouble()
    }

    fun randomInt(start: Int, end: Int): Number {
        return Random.nextInt(start, end + 1)
    }

    fun randomDouble(start: Double, end: Double): Number {
        return Random.nextDouble(start, end)
    }

    // 常量
    val PI: Number = kotlin.math.PI
    val E: Number = kotlin.math.E
}