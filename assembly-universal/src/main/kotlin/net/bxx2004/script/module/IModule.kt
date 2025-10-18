package net.bxx2004.script.module

/**
 * 模块安装包
 */
abstract class IModule {
    init {
        modules.add(this)
    }

    companion object {
        val modules = ArrayList<IModule>()
    }

    abstract fun name(): Array<String>
}