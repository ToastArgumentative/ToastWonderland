package pine.toast.library

import org.bukkit.plugin.Plugin
import pine.toast.library.adapters.WonderlandAdapters
import pine.toast.library.commands.CommandManager
import pine.toast.library.enchants.EnchantmentManager
import pine.toast.library.events.items.ItemEventManager
import pine.toast.library.events.made.CustomEventListeners

object Wonderland {

    private lateinit var plugin: Plugin
    private val adapters = WonderlandAdapters()
    private val commandManager = CommandManager()

    fun initialize(plugin: Plugin) {
        this.plugin = plugin

        plugin.server.pluginManager.registerEvents(CustomEventListeners(), plugin)
        plugin.server.pluginManager.registerEvents(ItemEventManager, plugin)
        plugin.server.pluginManager.registerEvents(EnchantmentManager, plugin)


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





}