package net.bxx2004.script.container

import net.bxx2004.assembly.data.Side

/**
 * @author 6hisea
 * @date  2026/2/12 15:36
 * @description: None
 */
data class ScriptUnit(
    val side: Side,
    val name: String,
    val script: String,
) {
}