package net.bxx2004.script.module

/**
 * 模块安装包
 */
abstract class IModule {
    abstract val isInject: Boolean
    init {
        modules.add(this)
    }

    companion object {
        val modules = ArrayList<IModule>()
    }

    abstract fun name(): Array<String>
}