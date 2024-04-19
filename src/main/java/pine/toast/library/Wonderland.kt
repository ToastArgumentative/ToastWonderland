package pine.toast.library

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import pine.toast.library.adapters.WonderlandAdapters
import pine.toast.library.commands.CommandAll
import pine.toast.library.commands.CommandManager
import pine.toast.library.enchants.EnchantmentManager
import pine.toast.library.entities.EntityManager
import pine.toast.library.events.CustomEventListeners
import pine.toast.library.inventories.InvManager
import pine.toast.library.items.ItemEventManager
import pine.toast.library.items.ItemManager
import pine.toast.library.utilities.RecipeManager
import pine.toast.library.utilities.WonderlandColors

object Wonderland {

    private lateinit var plugin: Plugin
    private const val VERSION = "1.0.9-preview11"
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

    @CommandAll
    private fun wlVersion(sender: CommandSender, args: List<String>) {
        sender.sendMessage(Component.text("${WonderlandColors.AQUA.code}Wonderland: $VERSION"))
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