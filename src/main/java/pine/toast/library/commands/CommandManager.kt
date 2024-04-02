package pine.toast.library.commands

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import pine.toast.library.Wonderland
import pine.toast.library.utilities.WonderlandColors
import java.lang.reflect.Method
import java.util.*
import java.util.logging.Level

class CommandManager {

    private val commands: MutableMap<String, Method> = mutableMapOf()

    fun registerCommands() {
        val methods = Wonderland.getPlugin().javaClass.declaredMethods
        for (method in methods) {
            if (!method.isAnnotationPresent(CommandConsole::class.java) &&
                !method.isAnnotationPresent(CommandPlayer::class.java) &&
                !method.isAnnotationPresent(CommandAll::class.java)
            ) {
                continue
            }

            val commandName = method.name.lowercase(Locale.getDefault())
            if (commands.containsKey(commandName)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Warning: Duplicate command '$commandName'. Skipping...")
                continue
            }
            commands[commandName] = method
        }

        println("Registered ${commands.size} commands.")

    }




    fun executeCommand(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val method = commands[label.lowercase(Locale.getDefault())] ?: return false

        val isConsoleCommand = method.isAnnotationPresent(CommandConsole::class.java)
        val isPlayerCommand = method.isAnnotationPresent(CommandPlayer::class.java)
        val isAllCommand = method.isAnnotationPresent(CommandAll::class.java)

        // If the command is marked as console-only and the sender is not the console, return false
        if (isConsoleCommand && sender !is ConsoleCommandSender) {
            sender.sendMessage(WonderlandColors.RED + "This command is only available in the console.")
            Wonderland.getPlugin().logger.log(Level.WARNING, "${sender.name} has tried to execute $label but does not have permission to do so.")
            return true
        }

        // If the command is marked as player-only and the sender is not a player, return false
        if (isPlayerCommand && sender !is Player) {
            Wonderland.getPlugin().logger.log(Level.WARNING, "This command is only available for players.")
            return false
        }

        // If the command is marked as CommandAll check whether the sender is a player or the console
        if (isAllCommand && (sender is Player || sender is ConsoleCommandSender)) {
            val annotation = method.getAnnotation(CommandAll::class.java) ?: return false
            val permission = annotation.permission
            val allOperators = annotation.allOperators

            if (permission.isNotEmpty() && !sender.hasPermission(permission)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "${sender.name} has tried to execute $label but does not have permission to do so.")
                return true
            }

            if (allOperators && !sender.isOp) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "${sender.name} has tried to execute $label but does not have permission to do so.")
                return true
            }
        }

        // Execute the command
        try {
            method.invoke(Wonderland.getPlugin(), sender, args)
        } catch (e: Exception) {
            Wonderland.getPlugin().logger.log(Level.SEVERE, "Error executing command '$label': ${e.message}")
        }

        return true
    }






}