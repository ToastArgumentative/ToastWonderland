package pine.toast.library.inventories

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class InvManager : Listener {

    private val memInventories: MutableMap<String, WLInventory> = mutableMapOf()

    fun registerInventory(inv: WLInventory) {
        val inventory = inv.inventory
        if (inv.doISaveInMem()) memInventories[inv.getIdentifier()] = inv

        val items: Map<Int, ItemStack> = inv.populateInventory()

        for (item in items) {
            inventory.setItem(item.key, item.value)
        }

    }

    /**
     * @param player Player The player to open the inventory for
     * @param identifier String The identifier of the inventory
     * @throws IllegalArgumentException if the inventory doesn't exist
     */
    fun openInventory(player: Player, identifier: String) {
        val inv = getInventory(identifier) ?: throw IllegalArgumentException("No inventory found with identifier $identifier")
        player.openInventory(inv.inventory)
    }

    fun getInventory(identifier: String): WLInventory? {
        return memInventories[identifier]
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun handleOpen(event: InventoryOpenEvent) {
        val inv = event.inventory
        val holder = inv.holder ?: return

        if (holder !is WLInventory) return

        holder.handleOpen(event)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun handleClick(event: InventoryClickEvent) {
        val inv = event.inventory
        val holder = inv.holder ?: return

        if (holder !is WLInventory) return

        holder.handleClick(event)

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun handleClose(event: InventoryCloseEvent) {
        val inv = event.inventory
        val holder = inv.holder ?: return

        if (holder !is WLInventory) return

        holder.handleClose(event)
    }

}