package pine.toast.library

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import pine.toast.library.adapters.WonderlandAdapters
import pine.toast.library.commands.simple.CommandManager
import pine.toast.library.commands.simple.CommandType
import pine.toast.library.commands.simple.WLCommand
import pine.toast.library.enchants.EnchantmentManager
import pine.toast.library.entities.EntityManager
import pine.toast.library.events.CustomEventListeners
import pine.toast.library.inventories.InvManager
import pine.toast.library.items.ItemEventManager
import pine.toast.library.items.ItemManager
import pine.toast.library.utilities.RecipeManager
import pine.toast.library.utilities.WonderlandColors
import java.util.logging.Level

object Wonderland {

    private lateinit var plugin: Plugin
    private const val VERSION = "1.0.9-preview13"
    private val adapters = WonderlandAdapters()
    private val commandManager = CommandManager()
    private val invManager = InvManager()

    fun initialize(plugin: Plugin) {
        this.plugin = plugin

        plugin.server.pluginManager.registerEvents(CustomEventListeners(), plugin)
        plugin.server.pluginManager.registerEvents(ItemEventManager, plugin)
        plugin.server.pluginManager.registerEvents(EnchantmentManager, plugin)
        plugin.server.pluginManager.registerEvents(EntityManager, plugin)
        plugin.server.pluginManager.registerEvents(ItemManager, plugin)
        plugin.server.pluginManager.registerEvents(invManager, plugin)

    }

    @WLCommand(target = CommandType.ALL, cooldown = 3)
    private fun wlVersion(sender: CommandSender, args: List<String>) {
        if (sender is Player) {
            sender.sendMessage(Component.text("${WonderlandColors.AQUA.code}Wonderland: $VERSION"))
        } else getPlugin().logger.log(Level.INFO, "${WonderlandColors.AQUA.code}Wonderland: $VERSION")
    }

    fun wlVersion(): String {
        return VERSION
    }


    fun shutDown() {
        RecipeManager.unregisterRecipes()
    }

    fun getPlugin(): Plugin {
        return plugin
    }

    fun getCommandManager(): CommandManager {
        return commandManager
    }

    fun getAdapters(): WonderlandAdapters {
        return adapters
    }

    fun getInvManager(): InvManager {
        return invManager
    }





}