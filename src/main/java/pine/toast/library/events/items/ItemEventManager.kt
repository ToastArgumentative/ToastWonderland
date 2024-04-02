package pine.toast.library.events.items

import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import pine.toast.library.Wonderland
import pine.toast.library.WonderlandKeys
import pine.toast.library.events.made.PlayerLeftClickEvent
import pine.toast.library.events.made.PlayerRightClickEvent

object ItemEventManager : Listener {


    /**
     * Stores an item handler in the item
     * @param item ItemStack The item to store the handler in
     * @param handler ItemHandler The handler to store
     */
    fun injectItemHandler(itemPDC: PersistentDataContainer, handler: ItemHandler) {
        itemPDC.set(WonderlandKeys.ITEM_HANDLER, Wonderland.getAdapters().itemHandlerAdapter, handler )
    }

    /**
     * Returns the item handler if there is one
     * @param item ItemStack The item to get the handler from
     */
    private fun getItemHandler(item: ItemStack): ItemHandler? {
        return item.itemMeta.persistentDataContainer.get(WonderlandKeys.ITEM_HANDLER, Wonderland.getAdapters().itemHandlerAdapter)
    }


    /**
     * handles an item event
     * @param item ItemStack The item to handle the event for
     */
    private fun handleItemEvent(item: ItemStack, event: Event) {
        val handler = getItemHandler(item) ?: return

        if (event is PlayerLeftClickEvent) {
            handler.onPlayerLeftClick(event)
        } else if (event is PlayerRightClickEvent) {
            handler.onPlayerRightClick(event)
        }
    }

    @EventHandler
    fun onPlayerLeftClick(event: PlayerLeftClickEvent) {
        val item = event.getPlayer().inventory.itemInMainHand
        if (item.type != Material.AIR) handleItemEvent(item, event)
    }

    @EventHandler
    fun onPlayerRightClick(event: PlayerRightClickEvent) {
        val item = event.getPlayer().inventory.itemInMainHand
        if (item.type != Material.AIR) handleItemEvent(item, event)
    }


}