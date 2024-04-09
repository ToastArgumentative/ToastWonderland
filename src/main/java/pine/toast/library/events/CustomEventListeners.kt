package pine.toast.library.events

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import pine.toast.library.Wonderland

class CustomEventListeners : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun callPlayerRightClickEvent(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return
        val player = event.player
        val mainHand: ItemStack = player.inventory.itemInMainHand
        val offHand: ItemStack = player.inventory.itemInOffHand

        Wonderland.getPlugin().server.pluginManager.callEvent(PlayerRightClickEvent(player, mainHand, offHand))

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun callPlayerLeftClickEvent(event: PlayerInteractEvent) {
        if (!event.action.isLeftClick) return
        val player = event.player
        val mainHand: ItemStack = player.inventory.itemInMainHand
        val offHand: ItemStack = player.inventory.itemInOffHand

        Wonderland.getPlugin().server.pluginManager.callEvent(PlayerLeftClickEvent(player, mainHand, offHand))
    }


}