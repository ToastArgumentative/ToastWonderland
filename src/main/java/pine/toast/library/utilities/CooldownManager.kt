package pine.toast.library.utilities

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.WonderlandKeys

object CooldownManager {

    /**
     * Applies a cooldown to the player in seconds.
     * @param player Player The player to apply the cooldown to
     * @param key String The namespace key for the cooldown
     * @param time Int in seconds
     */
    fun applyPlayerCooldown(player: Player, key: NamespacedKey, time: Int) {
        val cooldownTime = System.currentTimeMillis() + (time * 1000).toLong()

        player.persistentDataContainer.set(key, PersistentDataType.LONG, cooldownTime)
    }

    /**
     * Returns true if the player is on cooldown for the specified key.
     * @param player Player The player to check
     * @param key String The namespace key for the cooldown
     */
    fun isPlayerOnCooldown(player: Player, key: NamespacedKey): Boolean {
        checkPlayerCooldown(player, key)
        val cooldownTime = player.persistentDataContainer.get(key, PersistentDataType.LONG)
        return cooldownTime != null && cooldownTime > System.currentTimeMillis()
    }

    /**
     * Returns the remaining cooldown time for the player in milliseconds.
     * @param player Player The player to check
     * @param key String The namespace key for the cooldown
     */
    fun getPlayerRemainingCooldown(player: Player, key: NamespacedKey): Long {
        checkPlayerCooldown(player, key)
        val cooldownTime = player.persistentDataContainer.get(key, PersistentDataType.LONG) ?: return 0
        return cooldownTime - System.currentTimeMillis()
    }

    /**
     * Removes the cooldown for the specified key from the player.
     * @param player Player The player to remove cooldown from
     * @param key String The namespace key for the cooldown
     */
    fun removePlayerCooldown(player: Player, key: NamespacedKey) {
        player.persistentDataContainer.remove(key)
    }

    /**
     * Checks the cooldown status and if its invalid it will remove it
     * @param player Player The player to check and update cooldown for
     * @param key NamespacedKey The namespace key for the cooldown
     */
    private fun checkPlayerCooldown(player: Player, key: NamespacedKey) {
        val cooldownTime = player.persistentDataContainer.get(key, PersistentDataType.LONG)
        if (cooldownTime != null && cooldownTime <= System.currentTimeMillis()) {
            removePlayerCooldown(player, key)
        }
    }

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
        checkCooldownItem(item)
        val itemPDC = item.itemMeta.persistentDataContainer
        return itemPDC.has(WonderlandKeys.COOLDOWN, PersistentDataType.LONG)
    }

    /**
     * Returns whether the item is still on cooldown
     * @param item ItemStack The item to check
     */
    private fun checkCooldownItem(item: ItemStack): Boolean {
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
        checkCooldownItem(item)
        val itemMeta = item.itemMeta
        return itemMeta.persistentDataContainer.get(WonderlandKeys.COOLDOWN, PersistentDataType.LONG) ?: 0

    }

    /**
     * Updates the cooldown of an item
     * You can use a negative value to subtract the time, but if the cooldown goes negative
     * meaning the cooldown is over, you WILL HAVE TO RUN checkCooldownItem again.
     * @param item ItemStack The item to update
     * @param time Int in seconds
     */
    fun updateCooldownItem(item: ItemStack, time: Int) {
        val itemMeta = item.itemMeta
        val cooldownTime = System.currentTimeMillis() + (time * 1000).toLong()

        itemMeta.persistentDataContainer.set(WonderlandKeys.COOLDOWN, PersistentDataType.LONG, cooldownTime)

        item.itemMeta = itemMeta
        checkCooldownItem(item)
    }

}