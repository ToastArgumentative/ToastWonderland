package pine.toast.plugin

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import pine.toast.library.Wonderland
import pine.toast.library.commands.CommandPlayer
import pine.toast.library.enchants.EnchantmentManager
import pine.toast.library.events.items.ItemBlueprint
import pine.toast.library.utilities.RecipeManager
import pine.toast.library.utilities.RecipeShape
import pine.toast.library.utilities.ScoreboardManager
import pine.toast.library.utilities.ScreenManager
import java.util.*

@Suppress("unused")
class ToastWonderland : JavaPlugin(), Listener {

    private val plugin: Plugin = this

    override fun onEnable() {
        // Plugin startup logic

        Wonderland.initialize(plugin)
        Wonderland.getCommandManager().registerCommands()
        EnchantmentManager.registerEnchantment(SuperJump())
        EnchantmentManager.registerEnchantment(Healthy())
        server.pluginManager.registerEvents(this, plugin)

        easyDiamondRecipe()
    }

    @CommandPlayer(cooldown = 5, permission = "wonderland.no")
    fun test(sender: Player, args: Array<String>) {
        ScreenManager.sendScreenMessage(sender, args[0], null)

    }


    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.sendMessage("Welcome!")

        val attributes: MutableMap<AttributeModifier, Attribute> = mutableMapOf()
        val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
        val flags: MutableSet<ItemFlag> = mutableSetOf()
        val lore: MutableList<String> = mutableListOf()

        val attackDamageAttr = AttributeModifier(UUID.randomUUID(), "Attack Damage", 25.0, AttributeModifier.Operation.ADD_NUMBER)
        attributes[attackDamageAttr] = Attribute.GENERIC_ATTACK_DAMAGE

        enchantments[Enchantment.DAMAGE_ALL] = 30
        enchantments[Enchantment.KNOCKBACK] = 2

        flags.add(ItemFlag.HIDE_ENCHANTS)
        flags.add(ItemFlag.HIDE_UNBREAKABLE)
        flags.add(ItemFlag.HIDE_ATTRIBUTES)

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

        val lines: MutableList<String> = mutableListOf()
        lines.add("Hello world!")
        lines.add("Hello world!")
        lines.add("") // Spacer
        lines.add("Hello world!")
        lines.add("Hello world!")
        ScoreboardManager.createNewScoreboard(event.player, "Test", lines)


        val superJumpBook = EnchantmentManager.createEnchantBook("Super Jump")
        val healthyJumpBook = EnchantmentManager.createEnchantBook("Healthy")

        player.inventory.setItem(2, superJumpBook)
        player.inventory.setItem(3, healthyJumpBook)

    }


    fun easyDiamondRecipe() {

        val recipeShape = RecipeShape(
            Material.DIRT,
            Material.DIRT,
            null,
            Material.DIRT,
            Material.DIRT,
            null,
            Material.DIRT,
            Material.DIRT,
            null,

        )
        val result = ItemStack(Material.DIAMOND)
        val easydiamond = RecipeManager.createRecipe(recipeShape, result)

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return Wonderland.getCommandManager().executeCommand(sender, label, args)
    }


    override fun onDisable() {
        // Plugin shutdown logic
    }
}
