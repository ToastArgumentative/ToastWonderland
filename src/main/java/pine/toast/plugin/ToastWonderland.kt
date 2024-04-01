package pine.toast.plugin

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import org.w3c.dom.Attr
import pine.toast.library.ItemBlueprint
import java.util.*

class ToastWonderland : JavaPlugin() {


    override fun onEnable() {
        // Plugin startup logic


        val attributes: MutableMap<AttributeModifier, Attribute> = mutableMapOf()
        val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
        val flags: MutableSet<ItemFlag> = mutableSetOf()
        val lore: MutableList<String> = mutableListOf()

        val attackDamageAttr = AttributeModifier(UUID.randomUUID(), "Attack Damage", 2.0, AttributeModifier.Operation.ADD_NUMBER)
        attributes[attackDamageAttr] = Attribute.GENERIC_ATTACK_DAMAGE

        enchantments[Enchantment.DAMAGE_ALL] = 4
        enchantments[Enchantment.KNOCKBACK] = 2

        flags.add(ItemFlag.HIDE_ENCHANTS)
        flags.add(ItemFlag.HIDE_UNBREAKABLE)

        lore.add("I am the wonder of the world!")
        lore.add("I am the wonder of the world!")

        val blueprint = ItemBlueprint(
            "Toast's Sword",
            lore,
            Material.DIAMOND_SWORD,
            enchantments,
            attributes,
            flags
        )

        // Builds the item and returns an item stack
        val item = blueprint.build()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
