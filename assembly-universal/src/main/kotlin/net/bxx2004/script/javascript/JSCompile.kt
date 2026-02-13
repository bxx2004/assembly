package net.bxx2004.script.javascript

import net.bxx2004.script.ThorCompile
import net.bxx2004.script.source.ThorSource
import org.openjdk.nashorn.api.scripting.NashornScriptEngine
import java.io.Reader
import javax.script.CompiledScript
import javax.script.ScriptContext

class JSCompile(val source: ThorSource, val compile:CompiledScript):ThorCompile {
    override fun adapt(): Any {
        return compile
    }

    override fun source(): Reader {
        return source.reader()
    }

    override fun variable(): Map<String, Any?> {
        return compile.engine.getBindings(ScriptContext.ENGINE_SCOPE)
    }

    override fun injectVariable(variables: Map<String, Any?>) {
        variables.forEach{t,u->
            compile.engine.getBindings(ScriptContext.ENGINE_SCOPE)[t] = u
        }
    }
    override fun eval(variable: Map<String, Any?>): Any? {
        val binds = compile.engine.getBindings(ScriptContext.ENGINE_SCOPE)
        variable.forEach { t, u ->
            binds[t] = u
        }
        var a = compile.eval(binds)
        variable.forEach { t, _ ->
            binds.remove(t)
        }
        return a
    }

    override fun invokeFunction(
        name: String,
        args: List<Any?>
    ): Any? {
        return (compile.engine as NashornScriptEngine).invokeFunction(name,*args.toTypedArray())
    }
}