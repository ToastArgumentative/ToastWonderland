package pine.toast.library.commands

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
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

    private fun createCommandAndAddToMap() {
        val commandMap = Bukkit.getCommandMap()
        for ((commandName, commandData) in commands) {

            val officialCommand: Command = object : Command(commandName) {
                override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
                    return executeCommand(sender, commandLabel, args)
                }
            }
            // Register your command
            commandMap.register(Wonderland.getPlugin().name, officialCommand)
        }
    }

    fun registerCommands(vararg instances: Any) {
        // Clear previous commands
        commands.clear()

        // Iterate over each provided instance to register commands
        instances.forEach { instance ->
            registerCommandsFromInstance(instance)
        }
        // Optionally, include the main plugin instance if it also contains commands
        registerCommandsFromInstance(Wonderland.getPlugin())
        createCommandAndAddToMap()

        Wonderland.getPlugin().logger.log(Level.INFO, "Registered ${commands.size} commands.")
    }



    private fun registerCommandsFromInstance(instance: Any) {
        val methods = instance.javaClass.declaredMethods
        for (method in methods) {
            if (!method.isAnnotationPresent(CommandConsole::class.java) &&
                !method.isAnnotationPresent(CommandPlayer::class.java) &&
                !method.isAnnotationPresent(CommandAll::class.java)) continue

            val commandName = method.name.lowercase(Locale.getDefault())
            if (commands.containsKey(commandName)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Warning: Duplicate command '$commandName'. Skipping...")
                continue
            }
            commands[commandName] = Pair(method, instance)
        }
    }


    fun executeCommand(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val (method, instance) = commands[label.lowercase(Locale.getDefault())] ?: return false

        val isConsoleCommand = method.isAnnotationPresent(CommandConsole::class.java)
        val isPlayerCommand = method.isAnnotationPresent(CommandPlayer::class.java)
        val isAllCommand = method.isAnnotationPresent(CommandAll::class.java)

        // If the command is marked as console-only and the sender is not the console, return true to hide command usage print
        if (isConsoleCommand && sender !is ConsoleCommandSender) {
            sender.sendMessage(WonderlandColors.RED + "This command is only available in the console.")
            Wonderland.getPlugin().logger.log(
                Level.WARNING,
                "${sender.name} has tried to execute $label but does not have permission to do so."
            )
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
            val cooldownSeconds = annotation.cooldown

            if (permission.isNotEmpty() && !sender.hasPermission(permission)) {
                Wonderland.getPlugin().logger.log(
                    Level.SEVERE,
                    "${sender.name} has tried to execute $label but does not have permission to do so."
                )
                return true
            }

            if (cooldownSeconds > 0 && sender is Player) {
                val cooldownKey = NamespacedKey(Wonderland.getPlugin(), label)
                if (CooldownManager.isPlayerOnCooldown(sender, cooldownKey)) {
                    val remainingCooldown = CooldownManager.getPlayerRemainingCooldown(sender, cooldownKey)
                    sender.sendMessage(WonderlandColors.RED + "You are still on cooldown for this command. Time remaining: ${remainingCooldown / 1000} seconds")
                    return true
                } else {
                    CooldownManager.applyPlayerCooldown(sender, cooldownKey, cooldownSeconds)
                }
            }
        }

        if (isPlayerCommand && sender is Player) {
            val annotation = method.getAnnotation(CommandPlayer::class.java)
            val permission = annotation.permission
            val cooldownSeconds = annotation.cooldown

            if (permission.isNotEmpty() && !sender.hasPermission(permission)) {
                Wonderland.getPlugin().logger.log(
                    Level.SEVERE,
                    "${sender.name} has tried to execute $label but does not have permission to do so."
                )
                return true
            }

            if (cooldownSeconds > 0) {
                val cooldownKey = NamespacedKey(Wonderland.getPlugin(), label)
                if (CooldownManager.isPlayerOnCooldown(sender, cooldownKey)) {
                    val remainingCooldown = CooldownManager.getPlayerRemainingCooldown(sender, cooldownKey)
                    sender.sendMessage(WonderlandColors.RED + "You are still on cooldown for this command. Time remaining: ${remainingCooldown / 1000} seconds")
                    return true
                } else {
                    CooldownManager.applyPlayerCooldown(sender, cooldownKey, cooldownSeconds)
                }
            }
        }

        // Execute the command
        try {
            method.invoke(instance, sender, args)
        } catch (e: InvocationTargetException) {
            // If the method throws an exception, it's wrapped in InvocationTargetException
            if (e.targetException != null) {
                // Log the actual cause of the method exception
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Error executing command '$label': ${e.targetException.message}")
            } else {
                // This might be unnecessary if you're sure the command executed successfully
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Error executing command '$label': null")
            }
        }

        return true
    }


}