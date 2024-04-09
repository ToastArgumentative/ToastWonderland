package pine.toast.library.commands

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandConsole

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandPlayer(val permission: String = "", val allOperators: Boolean = false, val cooldown: Int = 0)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CommandAll(val permission: String = "", val allOperators: Boolean = false, val cooldown: Int = 0)