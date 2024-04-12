package pine.toast.library.inventories

import net.kyori.adventure.text.Component
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import pine.toast.library.Wonderland

abstract class WLInventory(
    private var rows: Int,
    private val name: Component,
    private val saveInMem: Boolean,
    private val identifier: String
) : InventoryHolder, Listener {

    private val inventory: Inventory = Wonderland.getPlugin().server.createInventory(this, 6*rows, name)


    abstract fun populateInventory(): Map<Int, ItemStack>

    abstract fun handleClick(event: InventoryClickEvent)

    abstract fun handleOpen(event: InventoryOpenEvent)

    abstract fun handleClose(event: InventoryCloseEvent)


    override fun getInventory(): Inventory {
        return this.inventory
    }


    fun getName(): Component {
        return name
    }

    fun doISaveInMem(): Boolean {
        return saveInMem
    }

    fun getRows(): Int {
        return rows
    }

    fun getIdentifier(): String {
        return identifier
    }


}