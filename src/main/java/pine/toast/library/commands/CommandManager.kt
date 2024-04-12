package pine.toast.library.commands

import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import pine.toast.library.Wonderland
import pine.toast.library.utilities.CooldownManager
import pine.toast.library.utilities.WonderlandColors
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.logging.Level

class CommandManager {

    private val commands: MutableMap<String, Pair<Method, Any>> = mutableMapOf()

    fun registerCommands(vararg instances: Any) {
        // Clear previous commands
        commands.clear()

        // Register commands from each instance
        instances.forEach { instance ->
            registerCommandsFromInstance(instance)
        }
        registerCommandsFromInstance(Wonderland.getPlugin())

        Wonderland.getPlugin().logger.log(Level.INFO, "Registered ${commands.size} commands.")
    }

    private fun registerCommandsFromInstance(instance: Any) {
        val methods = instance.javaClass.declaredMethods
        for (method in methods) {
            if (method.isAnnotationPresent(CommandConsole::class.java) ||
                method.isAnnotationPresent(CommandPlayer::class.java) ||
                method.isAnnotationPresent(CommandAll::class.java)) {

                val commandName = method.name.lowercase(Locale.getDefault())
                if (commands.containsKey(commandName)) {
                    Wonderland.getPlugin().logger.log(Level.SEVERE, "Warning: Duplicate command '$commandName'. Skipping...")
                    continue
                }
                commands[commandName] = Pair(method, instance)
            }
        }
    }

    fun executeCommand(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val (method, instance) = commands[label.lowercase(Locale.getDefault())] ?: return false

        val isConsoleCommand = method.isAnnotationPresent(CommandConsole::class.java)
        val isPlayerCommand = method.isAnnotationPresent(CommandPlayer::class.java)
        val isAllCommand = method.isAnnotationPresent(CommandAll::class.java)

        if (isConsoleCommand && sender !is ConsoleCommandSender) {
            sender.sendMessage(WonderlandColors.RED + "This command is only available in the console.")
            return true
        }

        if (isPlayerCommand && sender !is Player) {
            sender.sendMessage(WonderlandColors.RED + "This command is only available for players.")
            return false
        }

        if (isAllCommand) {
            val annotation = method.getAnnotation(CommandAll::class.java)
            if (!sender.hasPermission(annotation.permission)) {
                sender.sendMessage(WonderlandColors.RED + "You do not have permission to perform this command.")
                return true
            }

            if (annotation.cooldown > 0 && sender is Player) {
                val cooldownKey = NamespacedKey(Wonderland.getPlugin(), label)
                if (CooldownManager.isPlayerOnCooldown(sender, cooldownKey)) {
                    val remainingCooldown = CooldownManager.getPlayerRemainingCooldown(sender, cooldownKey)
                    sender.sendMessage(WonderlandColors.RED + "You are still on cooldown for this command. Time remaining: ${remainingCooldown / 1000} seconds")
                    return true
                } else {
                    CooldownManager.applyPlayerCooldown(sender, cooldownKey, annotation.cooldown)
                }
            }
        }

        return try {
            method.invoke(instance, sender, args) as Boolean
        } catch (e: IllegalAccessException) {
            Wonderland.getPlugin().logger.log(Level.SEVERE, "Illegal access while executing command '$label'", e)
            false
        } catch (e: InvocationTargetException) {
            Wonderland.getPlugin().logger.log(Level.SEVERE, "Error executing command '$label': ${e.targetException?.message}", e)
            false
        }
    }
}
