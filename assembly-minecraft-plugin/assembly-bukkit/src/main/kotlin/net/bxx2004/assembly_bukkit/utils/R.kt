package net.bxx2004.assembly_bukkit.utils

/**
 * @author 6hisea
 * @date  2026/2/23 18:56
 * @description: None
 */
data class R(
    val status: Boolean,
    val message: String
) {
    companion object{
        val OK = R(true,"> 任务执行成功")
        fun ok(msg: String): R {
            return R(true, msg)
        }
        fun error(msg: String): R {
            return R(false, msg)
        }
    }
}