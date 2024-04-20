package pine.toast.library.commands

enum class CommandType {
    PLAYER,
    CONSOLE,
    ALL
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class WLCommand(
    val target: CommandType = CommandType.ALL,
    val permission: String = "",
    val cooldown: Int = 0
)
