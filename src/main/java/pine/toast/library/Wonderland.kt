package pine.toast.library

import org.bukkit.plugin.Plugin
import pine.toast.library.adapters.WonderlandAdapters
import pine.toast.library.commands.CommandManager
import pine.toast.library.enchants.WLEnchantmentType
import pine.toast.library.enchants.WonderlandEnchantmentProps
import pine.toast.library.enchants.WLEnchantmentTarget
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

        val enchProps = WonderlandEnchantmentProps(
            "Super Sharpness",
            3,
            WLEnchantmentTarget.WEAPON,
            WLEnchantmentType.ON_HIT
        )

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