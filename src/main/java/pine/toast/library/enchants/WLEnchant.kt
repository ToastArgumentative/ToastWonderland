package pine.toast.library.enchants

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.ItemStack
import pine.toast.library.Wonderland
import pine.toast.library.WonderlandKeys
import pine.toast.library.utilities.WonderlandColors

abstract class WLEnchant {


    abstract val name: String
    abstract val level: Int
    abstract val targets: List<WLEnchantmentTarget>



    private fun checkApplicableStatus(item: ItemStack): Boolean {
        val material = item.type

        if (material == Material.AIR) return false
        for (target in targets) if(target.includes(item)) return true

        return false
    }

    fun applyEnchant(item: ItemStack) {
        if (!checkApplicableStatus(item)) throw IllegalStateException("$name is not applicable to $item")


        val itemMeta = item.itemMeta ?: return
        val storage = itemMeta.persistentDataContainer

        if (targets.any() { it.includes(item.type) }) {

            if (storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) {
                val storageData = storage.get(
                    WonderlandKeys.ENCHANTMENT_STORAGE,
                    Wonderland.getAdapters().enchantmentStorageAdapter
                )!!

                val enchantments = storageData.enchants

                enchantments[name.replace(" ", ".")] = level

                storage.set(
                    WonderlandKeys.ENCHANTMENT_STORAGE,
                    Wonderland.getAdapters().enchantmentStorageAdapter,
                    storageData
                )

            } else {
                val enchantments = mutableMapOf<String, Int>()
                enchantments[name.replace(" ", ".")] = level

                val enchantsStorage = EnchantmentStorage(enchantments)

                storage.set(
                    WonderlandKeys.ENCHANTMENT_STORAGE,
                    Wonderland.getAdapters().enchantmentStorageAdapter,
                    enchantsStorage
                )
            }

            val itemLore = item.lore() ?: mutableListOf()

            val loreComponents: MutableList<Component> = mutableListOf()
            var enchantName = name.replace(".", " ")
            enchantName = WonderlandColors.GRAY + enchantName + ": " + EnchantmentManager.intToRoman(level)

            loreComponents.add(Component.text(enchantName))

            itemLore.addAll(loreComponents)
            itemMeta.lore(itemLore)
            item.itemMeta = itemMeta

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
