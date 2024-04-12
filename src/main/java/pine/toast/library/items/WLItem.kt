package pine.toast.library.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag


abstract class WLItem {


    abstract fun getName(): Component

    abstract fun getLore(): MutableList<Component>

    abstract fun getMaterial(): Material

    abstract fun getEnchantments(): MutableMap<Enchantment, Int>

    abstract fun getAttributes(): MutableSet<AttributeModifier>

    abstract fun getFlags(): Set<ItemFlag>


    abstract fun getHandler(): ItemHandler





}