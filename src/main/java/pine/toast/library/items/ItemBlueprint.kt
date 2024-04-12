package pine.toast.library.items

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
 * @param handler Class<out ItemHandler> The event handlers for the item, this can be null
 * Once the item blueprint is populated you can use ItemBlueprint.build() to build the item
 */
@Deprecated("Use the WLItem class instead")
data class ItemBlueprint(
    val itemName: String?,
    val itemLore: MutableList<String>?,
    val material: Material,
    val enchantments: MutableMap<Enchantment, Int>?,
    val attributes: MutableMap<AttributeModifier, Attribute>?,
    val flags: Set<ItemFlag>?,
    val handler: Class<out ItemHandler>?

) {

    /**
     * Creates an item from the blueprint
     */
    fun build(): ItemStack {
        val item = ItemStack(material)
        val meta: ItemMeta? = item.itemMeta

        itemName?.let { meta?.displayName(stringToComponent(itemName)) }
        itemLore?.let { meta?.lore(itemLore.map { stringToComponent(it) }) }

        enchantments?.forEach() { meta?.addEnchant(it.key, it.value, true) }
        attributes?.forEach() { meta?.addAttributeModifier(it.value, it.key) }
        flags?.forEach() { meta?.addItemFlags(it) }

        if (handler != null) {
            val handlerInstance = handler.getConstructor().newInstance()
            ItemEventManager.injectItemHandler(meta!!.persistentDataContainer, handlerInstance)
        }

        meta?.let { item.itemMeta = it }

        return item
    }

    private fun stringToComponent(string: String): Component {
        return Component.text(string)
    }

}