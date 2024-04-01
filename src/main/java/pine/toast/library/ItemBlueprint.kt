package pine.toast.library

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta


/**
 * This class is used to build items in a nice organized way
 * @param itemName String The name of the item
 * @param itemLore MutableList<String> The lore of the item can be null
 * @param material Material The material of the item
 * @param enchantments MutableMap<Enchantment, Int> The enchantments of the item can be null
 * @param attributes MutableMap<AttributeModifier, Attribute> The attributes of the item can be null
 * @param flags Set<ItemFlag> The flags of the item can be null
 * Once the item blueprint is populated you can use ItemBlueprint.build() to build the item
 */
data class ItemBlueprint(
    val itemName: String?,
    val itemLore: MutableList<String>?,
    val material: Material,
    val enchantments: MutableMap<Enchantment, Int>?,
    val attributes: MutableMap<AttributeModifier, Attribute>?,
    val flags: Set<ItemFlag>?

) {

    /**
     * Creates an item from the blueprint
     */
    fun build(): ItemStack {
        val item = ItemStack(material)
        val meta: ItemMeta? = item.itemMeta

        itemName?.let { meta?.displayName(nameToComponent(itemName)) }
        itemLore?.let { meta?.lore(itemLore.map { nameToComponent(it) }) }

        enchantments?.forEach() { meta?.addEnchant(it.key, it.value, true) }
        attributes?.forEach() { meta?.addAttributeModifier(it.value, it.key) }
        flags?.forEach() { meta?.addItemFlags(it) }

        meta?.let { item.itemMeta = it }

        return item


    }

    private fun nameToComponent(name: String): Component {
        return Component.text(name)
    }

}