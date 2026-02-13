package net.bxx2004.assembly.application.resource.ehc

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import net.bxx2004.assembly.application.resource.ehc.parsers.JavaScriptParser
import net.bxx2004.assembly.application.resource.ehc.parsers.StringParser
import net.bxx2004.assembly.data.attribute.Attribute
import net.bxx2004.assembly.data.attribute.value
import net.bxx2004.assembly.utils.findAllFields
import net.bxx2004.assembly.utils.warn
import java.io.InputStream

/**
 * @author 6hisea
 * @date  2026/1/8 14:47
 * @description: None
 */
interface ElementParser<T> {
    fun parse(key:String,value: ConfigValue): Attribute<T>
}

inline fun <reified T>parse(conf: Config):T{
    val obj = T::class.java.getDeclaredConstructor().newInstance()
    obj::class.java.findAllFields().forEach { field ->
        field.isAccessible = true
        val anno = field.declaredAnnotations.filterIsInstance<Parser>().firstOrNull()
        var name = if (anno != null && anno.name != ""){
            anno.name
        }else{
            field.name
        }
        val value = if (conf.hasPath(name)){
            conf.getValue(name)
        }else if(conf.hasPath("~$name")){
            name = "~$name"
            conf.getValue(name)
        }else{
            null
        }

        var p = if (anno != null){
            val ins = Class.forName(anno.cls).getDeclaredField("INSTANCE").apply {
                isAccessible = true
            }.get(null)
            ins as ElementParser<*>
        }else{
            StringParser
        }
        if (name.startsWith("~")){
            p = JavaScriptParser
        }

        if (value != null){
            if (anno?.wrapper == false){
                if (name.substring(0,2) != "~~" && name.startsWith("~")){
                    warn("请注意: ${name.replace("~","")} 仅支持运算一次")
                }
                field.set(obj,p.parse(name,
                    value
                ).value)
            }else{
                field.set(obj,p.parse(name,
                    value
                ))
            }
        }
    }
    return obj
}

inline fun <reified T>parse(stream: InputStream): T {
    val conf = ConfigFactory.parseReader(stream.reader())
    return parse(conf)
}

fun <T>parse(cls: Class<T>, conf: Config):T{
    val obj = cls.getDeclaredConstructor().newInstance()
    obj::class.java.findAllFields().forEach { field ->
        field.isAccessible = true
        val anno = field.declaredAnnotations.filterIsInstance<Parser>().firstOrNull()

        var name = if (anno != null && anno.name != ""){
            anno.name
        }else{
            field.name
        }
        val value = if (conf.hasPath(name)){
            conf.getValue(name)
        }else if(conf.hasPath("~$name")){
            name = "~$name"
            conf.getValue(name)
        }else{
            null
        }
        var p = if (anno != null){
            val ins = Class.forName(anno.cls).getDeclaredField("INSTANCE").apply {
                isAccessible = true
            }.get(null)
            ins as ElementParser<*>

        }else{
            StringParser
        }
        if (name.startsWith("~")){
            p = JavaScriptParser
        }

        if (value != null){
            if (anno?.wrapper == false){
                if (name.substring(0,2) != "~~" && name.startsWith("~")){
                    warn("请注意: ${name.replace("~","")} 仅支持运算一次")
                }
                field.set(obj,p.parse(name,
                    value
                ).value)
            }else{
                field.set(obj,p.parse(name,
                    value
                ))
            }
        }
    }
    return obj
}
fun <T>parse(cls: Class<T>, stream: InputStream): T {
    val conf = ConfigFactory.parseReader(stream.reader())
    return parse(cls,conf)
}
