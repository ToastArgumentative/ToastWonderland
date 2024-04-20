package pine.toast.library.enchants

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import pine.toast.library.Wonderland
import pine.toast.library.WonderlandKeys
import pine.toast.library.utilities.ItemTarget
import pine.toast.library.utilities.WonderlandColors

abstract class WLEnchant {


    abstract val name: String
    abstract val targets: List<ItemTarget>


    private fun checkApplicableStatus(item: ItemStack): Boolean {
        val material = item.type

        if (material == Material.AIR) return false
        for (target in targets) if (target.includes(item)) return true

        return false
    }

    fun getLevelFromItem(itemMeta: ItemMeta): Int {
        val storage = itemMeta.persistentDataContainer
        val enchantmentStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter) ?: return 0
        val enchants = enchantmentStorage.enchants

        return enchants[name.replace(" ", ".")] ?: 0
    }

    /**
     * This function adds the enchant level to the item
     * the math of this ( currentLevel - level ) + level
     * This means that the current level of the item is subtracted by the level and then adds the current level. This follows minecraft standard enchant upgrading.
     * @param itemMeta ItemMeta The item to add the level to
     * @param level Int The level to add
     */
    fun addLevelToItem(itemStack: ItemStack, level: Int) {
        val itemMeta = itemStack.itemMeta
        val storage = itemMeta.persistentDataContainer
        val enchantmentStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter) ?: return
        val enchantments = enchantmentStorage.enchants

        val currentLevel = getLevelFromItem(itemMeta)

        enchantments[name.replace(" ", ".")] = calculateNewLevel(currentLevel, level)

        storage.set(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter, enchantmentStorage)

        val itemLore = itemMeta.lore() ?: return
        var enchantName = name.replace(".", " ")
        val prevEnchantName = Component.text("${WonderlandColors.GRAY.code}$enchantName")
        enchantName = "${WonderlandColors.GRAY.code}$enchantName ${EnchantmentManager.intToRoman(calculateNewLevel(enchantments[name.replace(" ", ".")] ?: 0, level))}"


        val newLore = mutableListOf<Component>()

        for (loreLine in itemLore) {
            if (loreLine == prevEnchantName) {
                newLore.add(Component.text(enchantName))
            } else newLore.add(loreLine)
        }

        itemMeta.lore(newLore)
        itemStack.itemMeta = itemMeta
    }

    private fun calculateNewLevel(existingLevel: Int, bookEnchantLevel: Int): Int {
        return when {
            bookEnchantLevel == existingLevel -> existingLevel + 1
            bookEnchantLevel > existingLevel -> existingLevel + (bookEnchantLevel - existingLevel)
            else -> existingLevel
        }
    }



    fun applyEnchant(item: ItemStack, bookEnchantLevel: Int) {
        if (!checkApplicableStatus(item)) throw IllegalStateException("$name is not applicable to $item")

        val itemMeta = item.itemMeta ?: return
        val storage = itemMeta.persistentDataContainer

        // Ensure the item type is supported by the enchantment
        if (targets.any { it.includes(item.type) }) {
            val enchantments = if (storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) {
                // If enchantment data already exists, fetch and modify it
                storage.get(
                    WonderlandKeys.ENCHANTMENT_STORAGE,
                    Wonderland.getAdapters().enchantmentStorageAdapter
                )!!.enchants
            } else {
                // If no enchantment data exists, initialize a new map
                mutableMapOf()
            }

            if (enchantments.isEmpty()) {
                val enchantsStorage = EnchantmentStorage(enchantments)
                storage.set(
                    WonderlandKeys.ENCHANTMENT_STORAGE,
                    Wonderland.getAdapters().enchantmentStorageAdapter,
                    enchantsStorage
                )

                val lore = itemMeta.lore() ?: mutableListOf(
                    Component.text("${WonderlandColors.GRAY.code}$name ${EnchantmentManager.intToRoman(bookEnchantLevel)}")
                )

                itemMeta.lore(lore)
                item.itemMeta = itemMeta

            } else {




            }

        }
    }


    abstract fun onHit(player: Player, level: Int, target: Entity)

    abstract fun onDamageTake(player: Player, level: Int, cause: DamageCause, amount: Double)

    abstract fun onBlockBreak(player: Player, level: Int, block: Block)

    abstract fun onItemUse(player: Player, level: Int, clickedBlock: Block?, action: Action)

    abstract fun onItemConsume(player: Player, level: Int, item: ItemStack)

    abstract fun onItemDrop(player: Player, level: Int, droppedItem: ItemStack)

    abstract fun onDeath(player: Player, level: Int, killer: Player?)

    abstract fun onKill(player: Player, level: Int, killedEntity: Entity)

    abstract fun onProjectileHit(player: Player, level: Int, target: Entity, hitBlock: Block?, hitTarget: Entity?)

    abstract fun onHeal(player: Player, level: Int, healAmount: Double )

    abstract fun onExpGain(player: Player, level: Int, xpAmount: Int)

    abstract fun onPlayerMove(player: Player, level: Int)
}
