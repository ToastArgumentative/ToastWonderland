package pine.toast.library.utilities

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.WonderlandKeys

object CooldownManager {


    /**
     * Applies a cooldown to the item in seconds.
     * @param item ItemStack The item to apply the cooldown to
     * @param time Int in seconds
     * Note: This actually just converts the time to long format and adds it to the items PDC
     */
    fun applyCooldownItem(item: ItemStack, time: Int) {
        val cooldownTime = System.currentTimeMillis() + (time * 1000).toLong()

        val itemMeta = item.itemMeta

        val itemPDC = itemMeta.persistentDataContainer
        itemPDC.set(WonderlandKeys.COOLDOWN, PersistentDataType.LONG, cooldownTime)

        item.itemMeta = itemMeta

    }

    /**
     * Returns true if the item has a cooldown
     * @param item ItemStack The item to check
     */
    fun isOnCooldownItem(item: ItemStack): Boolean {
        val itemPDC = item.itemMeta.persistentDataContainer
        checkCooldownItem(item)
        return itemPDC.has(WonderlandKeys.COOLDOWN, PersistentDataType.LONG)
    }

    /**
     * Returns whether the item is still on cooldown
     * @param item ItemStack The item to check
     */
    fun checkCooldownItem(item: ItemStack): Boolean {
        val itemMeta = item.itemMeta
        val cooldownTime = itemMeta.persistentDataContainer.get(WonderlandKeys.COOLDOWN, PersistentDataType.LONG) ?: return false

        if (cooldownTime > System.currentTimeMillis()) {
            return true
        } else {
            itemMeta.persistentDataContainer.remove(WonderlandKeys.COOLDOWN)
            item.itemMeta = itemMeta
            return false
        }
    }

    /**
     * Returns the cooldown time of an item in Long format
     * @param item ItemStack The item to check
     */
    fun getCooldownTimeItem(item: ItemStack): Long {
        val itemMeta = item.itemMeta
        return itemMeta.persistentDataContainer.get(WonderlandKeys.COOLDOWN, PersistentDataType.LONG) ?: 0

    }

    /**
     * Updates the cooldown of an item
     * You can use a negative value to subtract the time, but if the cooldown goes negative
     * meaning the cooldown is over, you WILL HAVE TO RUN checkCooldownItem again.
     * best practice is whenever you update always check
     * @param item ItemStack The item to update
     * @param time Int in seconds
     */
    fun updateCooldownItem(item: ItemStack, time: Int) {
        val itemMeta = item.itemMeta
        val cooldownTime = System.currentTimeMillis() + (time * 1000).toLong()

        itemMeta.persistentDataContainer.set(WonderlandKeys.COOLDOWN, PersistentDataType.LONG, cooldownTime)

        item.itemMeta = itemMeta
    }

}