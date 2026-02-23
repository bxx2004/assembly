package net.bxx2004.assembly_bukkit.utils

import net.bxx2004.assembly.application.AssemblyApplication
import net.bxx2004.assembly.application.AssemblyApplication.Companion.instances
import net.bxx2004.assembly.application.server.ServerInstanceManager.addInstance
import net.bxx2004.assembly.application.server.ServerInstanceManager.removeInstance
import net.bxx2004.assembly.application.server.ServerInstanceManager.removeInstanceById
import net.bxx2004.assembly.data.AssemblyIdentifier
import net.bxx2004.assembly_bukkit.BukkitSender.Companion.asPacketSender
import org.bukkit.entity.Player

/**
 * @author 6hisea
 * @date  2026/2/23 18:55
 * @description: None
 */
fun Player.addInstance(app: AssemblyApplication, id: AssemblyIdentifier,reload: Boolean = true): R {
    val tar = app.instances.find { it.id == id }
    if (tar == null) return R.error("应用 ${app.id} 中不存在标识符为 $id 的实例")
    if (reload){
        removeInstance(app,id)
    }
    app.addInstance(tar,asPacketSender)
    return R.OK
}
fun Player.removeInstance(app: AssemblyApplication, id: AssemblyIdentifier): R {
    val tar = app.instances.find { it.id == id }
    if (tar == null) return R.error("应用 ${app.id} 中不存在标识符为 $id 的实例")
    app.removeInstanceById(tar.id,asPacketSender)
    return R.OK
}