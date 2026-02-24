package net.bxx2004.assembly_bukkit.tlibm.commondhelper

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.CommandStructure
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentDynamic
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.module.chat.ComponentText
import taboolib.module.chat.colored
import taboolib.module.chat.component
import taboolib.module.chat.uncolored
import kotlin.math.ceil

/**
 * 多语言命令帮助模板
 * @author bxx2004
 * @since 2024/02/14 20:08
 */
fun newCommand(
    name: String,
    aliases: List<String> = emptyList(),
    description: String = "",
    usage: String = "",
    permission: String = "",
    permissionMessage: String = "",
    permissionDefault: PermissionDefault = PermissionDefault.OP,
    permissionChildren: Map<String, PermissionDefault> = emptyMap(),
    newParser: Boolean = false,
    checkPermission: Boolean = true,
    lang: String = "zh_cn",
    commandBuilder: CommandBase.() -> Unit,
) {
    command(
        name,
        aliases,
        description,
        usage,
        permission,
        permissionMessage,
        permissionDefault,
        permissionChildren,
        newParser
    ) {
        createHelper(checkPermission, lang)
        commandBuilder(this)
    }
}

fun CommandContext<out Any>.error(msg: String, index: Int = -1, lang: String = "zh_cn") {
    val errorIndex = if (index == -1) args().lastIndex else index
    var arg = "\n&c/$name ${args().joinToString(" ").replace(args()[errorIndex], "&c&l&n" + args()[errorIndex])}&c"
    var joinArgs = arg
    if (joinArgs.length > 25) {
        if (errorIndex <= 1) {
            joinArgs = arg.substring(0, 25).replace(args()[errorIndex], "&c&l&n" + args()[errorIndex] + "&c")
        } else {
            joinArgs = "&c/$name ... &c&l&n${args()[errorIndex]}&c ..."
        }
    }
    joinArgs += "  &7&o->${Language[lang, "error"]}\n"
    val fixIndex = joinArgs.colored().uncolored().indexOf(args()[errorIndex]) + args()[errorIndex].length / 2
    joinArgs += "&c${space(fixIndex - 1)} └── "
    val message = ArrayList<String>()
    for (i in 0..ceil(msg.length / 10.00).toInt()) {
        try {
            message.add(
                try {
                    msg.substring(i * 10, i * 10 + 10)
                } catch (e: Exception) {
                    msg.substring(i * 10, msg.length)
                }
            )
        } catch (e: Exception) {

        }
    }
    joinArgs += message[0] + "\n"
    for ((index1, s) in message.withIndex()) {
        if (index1 != 0) {
            joinArgs += "&c${space(fixIndex - 1)} $s"
        }
    }
    sender().sendMessage(joinArgs.colored())
}

private object Language {
    operator fun get(lang: String, node: String): String {
        when (lang) {
            "简体中文", "chinese", "zh_cn" -> {
                return when (node) {
                    "command" -> "命令"
                    "alias" -> "别名"
                    "permission" -> "权限"
                    "parameter" -> "参数"
                    "error" -> "发生错误"
                    else -> return "&c-$node($lang)-".colored()
                }
            }

            "繁体中文", "chinese_tw", "zh_tw" -> {
                return when (node) {
                    "command" -> "命令"
                    "alias" -> "别名"
                    "permission" -> "許可權"
                    "parameter" -> "參數"
                    "error" -> "發生錯誤"
                    else -> return "&c-$node($lang)-".colored()
                }
            }

            else -> return node
        }
    }
}


private val map = HashMap<CommandComponent, String>()
private var CommandComponent.description: String
    set(value) {
        map[this] = value
    }
    get() = map[this] ?: " "

fun CommandComponent.description(value: String) {
    description = value
}

private fun space(space: Int): String {
    return (1..space).joinToString("") { " " }
}

fun CommandComponent.noneParameters(message: String = "命令参数不足.") {
    execute<ProxyCommandSender> { sender, context, argument ->
        sender.sendMessage(message.colored())
    }
}

fun CommandComponent.offline(message: String = "目标玩家不在线.") {
    execute<ProxyCommandSender> { sender, context, argument ->
        sender.sendMessage(message.colored())
    }
}

fun CommandComponent.createHelper(check: Boolean = true, lang: String = "chinese"): CommandComponent {
    fun name(cmd: CommandComponent, base: String): String {
        return when (cmd) {
            is CommandComponentLiteral -> {
                cmd.aliases[0]
            }

            is CommandComponentDynamic -> {
                "<${cmd.comment}>"
            }

            else -> {
                base
            }
        }
    }

    fun preLink(cmd: CommandComponent, base: String): String {
        var result = ArrayList<String>()
        if (cmd.parent == null) {
            result += name(cmd, base)
        } else {
            result += name(cmd.parent!!, base)
            result += preLink(cmd.parent!!, base)
        }
        return result.distinct().reversed().joinToString(" ")
    }

    fun nextLink(cmd: CommandComponent, base: String): String {
        if (cmd.children.isEmpty()) {
            return name(cmd, base)
        }
        var builder = ArrayList<String>()
        builder.add(name(cmd, base))
        if (cmd.children.size == 1) {
            builder.add(nextLink(cmd.children[0], base))
        } else {
            builder.add("\\[...\\]")
        }
        return if (builder.joinToString(" ").length >= 30) {
            builder.joinToString(" ").substring(0, 30) + "..."
        } else {
            builder.joinToString(" ")
        }
    }

    fun print(
        children: List<CommandComponent>,
        builder: ComponentText,
        sender: ProxyCommandSender,
        command: CommandStructure
    ) {
        children.forEach {
            when (it) {
                is CommandComponentLiteral -> {
                    builder.append(
                        "    &7- [&7${nextLink(it, command.name)}](hover=${
                            it.aliases.filter { index != 0 }.joinToString(",")
                        };cmd=/${preLink(it, command.name) + " " + it.aliases[0]})\n".component().buildColored()
                    )
                }

                is CommandComponentDynamic -> {
                    builder.append(
                        "    &7- [&7${nextLink(it, command.name)}](hover=${
                            it.comment.replace("(", "\\(").replace(")", "\\)")
                        };suggest=/${preLink(it, command.name)})\n".component().buildColored()
                    )
                }
            }
            builder.append("      &f[${it.description}](hover=${it.permission})\n".component().buildColored())
        }
        builder.sendTo(sender)
    }
    execute<ProxyCommandSender> { sender, context, argument ->
        val command = context.command
        val builder = "\n&7&l ${Language[lang, "command"]}: &c&l/${command.name}".component().buildColored()
        if (command.aliases.isNotEmpty()) {
            builder.append(" &f[ ${Language[lang, "alias"]}: &b&L${command.aliases.joinToString()}&f ] ".colored())
        }
        if (command.permission.isNotEmpty()) {
            builder.append(" &f[ ${Language[lang, "permission"]}: &b&L${command.permission}&f ] ".colored())
        }
        builder.append("\n")
        parent?.let {
            when (this) {
                is CommandComponentLiteral -> {
                    builder.append(
                        space(2) + "§c⇨ &7“${
                            preLink(
                                this,
                                command.name
                            )
                        } ${this.aliases[0]}”...\n".colored()
                    )
                }

                is CommandComponentDynamic -> {
                    builder.append(space(2) + "§c⇨ &7“${preLink(this, command.name)} ${this.comment}”...\n".colored())
                }

                else -> {
                    builder.append(space(2) + "§c⇨ &7“${preLink(this, command.name)}”...\n".colored())
                }
            }
        }
        builder.append(space(4) + "&f&l${Language[lang, "parameter"]}:\n".colored())
        fun check(children: List<CommandComponent>): List<CommandComponent> {
            // 检查权限
            val filterChildren = if (check) {
                children.filter { sender.hasPermission(it.permission) }
            } else {
                children
            }
            // 过滤隐藏
            return filterChildren.filter { it !is CommandComponentLiteral || !it.hidden }
        }
        print(check(children), builder, sender, command)
    }
    return this
}

fun CommandComponent.literalWithHelper(
    vararg aliases: String,
    optional: Boolean = false,
    permission: String = "",
    hidden: Boolean = false,
    checkPermission: Boolean = true,
    lang: String = "zh_cn",
    literal: CommandComponentLiteral.() -> Unit = {},
): CommandComponentLiteral {
    return literal(
        aliases = aliases,
        optional = optional,
        permission = permission,
        hidden = hidden,
        literal = literal
    ).createHelper(checkPermission, lang) as CommandComponentLiteral
}

fun CommandComponent.dynamicWithHelper(
    comment: String = "...",
    optional: Boolean = false,
    permission: String = "",
    checkPermission: Boolean = true,
    lang: String = "zh_cn",
    dynamic: CommandComponentDynamic.() -> Unit = {},
): CommandComponentDynamic {
    return dynamic(comment, optional, permission, dynamic).createHelper(
        checkPermission,
        lang
    ) as CommandComponentDynamic
}