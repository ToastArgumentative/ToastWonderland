package pine.toast.library

import org.bukkit.plugin.Plugin
import pine.toast.library.adapters.WonderlandAdapters
import pine.toast.library.events.items.ItemEventManager
import pine.toast.library.events.made.CustomEventListeners

object Wonderland {

    private lateinit var plugin: Plugin
    private val adapters = WonderlandAdapters()

    fun initialize(plugin: Plugin) {
        this.plugin = plugin

        plugin.server.pluginManager.registerEvents(CustomEventListeners(), plugin)
        plugin.server.pluginManager.registerEvents(ItemEventManager, plugin)
    }

    fun getPlugin(): Plugin {
        return plugin
    }


    fun getAdapters(): WonderlandAdapters {
        return adapters
    }



}