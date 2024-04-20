package pine.toast.library.commands.advanced

abstract class WLCommand {

    abstract val name: String
    abstract val permission: String
    abstract val cooldown: Int


    abstract fun execute()

}