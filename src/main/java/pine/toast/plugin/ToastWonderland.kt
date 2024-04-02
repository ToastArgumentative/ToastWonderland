package pine.toast.plugin

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin
import pine.toast.library.Wonderland
import pine.toast.library.events.items.ItemBlueprint
import pine.toast.library.events.made.PlayerLeftClickEvent
import pine.toast.library.events.made.PlayerRightClickEvent
import java.util.*

class ToastWonderland : JavaPlugin(), Listener {


    override fun onEnable() {
        // Plugin startup logic

        Wonderland.initialize(this)
        server.pluginManager.registerEvents(this, this)


    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.sendMessage("Welcome!")

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
            flags,
            ToastsSwordHandler::class.java
        )

        // Builds the item and returns an item stack
        val item = blueprint.build()

        player.inventory.setItem(0, item)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
