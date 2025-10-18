package net.bxx2004.script.javascript

import net.bxx2004.script.*
import net.bxx2004.script.error.ScriptTypeException
import net.bxx2004.script.module.InstallAPI
import net.bxx2004.script.source.ThorSource
import org.openjdk.nashorn.api.scripting.NashornScriptEngine
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import javax.script.ScriptContext


class JSShell(val options: ThorOptions = ThorOptions.default()):ThorShell {
    private val engine = NashornScriptEngineFactory().getScriptEngine(options.CLASS_LOADER) as NashornScriptEngine
    init {
        injectVariable(hashMapOf(Pair("install", InstallAPI(this))))
    }
    override fun options(): ThorOptions {
        return options
    }
    override fun getVariable(name: String): Any? {
        return engine.getBindings(ScriptContext.ENGINE_SCOPE)[name]
    }
    override fun eval(source: ThorSource, variable: Map<String, Any?>, executor: ThorExecutor): Any? {
        val bindings = engine.createBindings()
        if (options.ENABLE_CACHE) {
            bindings.putAll(engine.getBindings(ScriptContext.ENGINE_SCOPE))
        }
        variable.forEach { t, u ->
            bindings[t] = u
        }
        bindings["executor"] = executor
        if (bindings.contains("main")){
            engine.getBindings(ScriptContext.ENGINE_SCOPE)["main"] = bindings["main"]
        }
        return engine.eval(source.reader(), bindings)
    }

    override fun invokeFunction(
        compile: ThorCompile?,
        name: String,
        args: List<Any?>,
        variable: Map<String, Any?>
    ): Any? {
        if (compile != null){
            if (compile.adapt() !is JSCompile){
                throw ScriptTypeException("ThorCompile is not a valid CompiledScript.")
            }
            return compile.invokeFunction(name,args)
        }else{
            return engine.invokeFunction(name,*args.toTypedArray())
        }
    }

    override fun injectVariable(variable: Map<String, Any?>) {
        variable.forEach{t,u->
            engine.getBindings(ScriptContext.ENGINE_SCOPE)[t] = u
        }
    }

    override fun removeVariable(name: String) {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).remove(name)
    }
    override fun compile(source: ThorSource, variable: Map<String, Any?>): ThorCompile {
        injectVariable(variable)
        val result = JSCompile(source,engine.compile(source.reader()))
        variable.forEach{t,_ ->
            removeVariable(t)
        }
        return result
    }
    override fun injectFunction(function: ThorFunction,cache:Boolean) {
        if (function !is JSFunction){
            throw ScriptTypeException("ThorFunction is not a valid JSFunction")
        }
        engine.eval(function.compile())
    }

    override fun close() {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).clear()
    }

    override fun type(): ThorType {
        return JSType
    }
}