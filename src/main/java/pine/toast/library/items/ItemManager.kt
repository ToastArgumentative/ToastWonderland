package pine.toast.library.items

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import pine.toast.library.Wonderland
import pine.toast.library.events.PlayerLeftClickEvent
import pine.toast.library.events.PlayerRightClickEvent
import java.lang.reflect.Method

data class ItemMethods(val methods: Set<Method>)


object ItemManager : Listener {

    private val handlerKey = NamespacedKey(Wonderland.getPlugin(), "item_handler")

    @EventHandler
    private fun onPlayerLeftClick(event: PlayerLeftClickEvent) {
        val item = event.getPlayer().inventory.itemInMainHand
        if (item.type == Material.AIR) return
        val itemPdc = item.itemMeta.persistentDataContainer

        val itemHandler = itemPdc.get(handlerKey, Wonderland.getAdapters().itemHandlerAdapter) ?: return
        itemHandler.onPlayerLeftClick(event)

    }

    @EventHandler
    private fun onPlayerRightClick(event: PlayerRightClickEvent) {
        val item = event.getPlayer().inventory.itemInMainHand
        if (item.type == Material.AIR) return
        val itemPdc = item.itemMeta.persistentDataContainer

        val itemHandler = itemPdc.get(handlerKey, Wonderland.getAdapters().itemHandlerAdapter) ?: return
        itemHandler.onPlayerRightClick(event)
    }


    /**
     * @param item WLItem The item to create
     * This adds everything into the stack and returns it.
     * Also handles your onLeftClick and onRightClick methods
     */
    fun createItem(item: WLItem): ItemStack {
        val itemClass = item::class.java
        val methods = itemClass.declaredMethods

        val methodSet: MutableSet<Method> = mutableSetOf()

        for (method in methods) {
            if (method.name == "onLeftClick" || method.name == "onRightClick") {
                method.isAccessible = true
                methodSet.add(method)
            }
        }

        val itemHandler: ItemHandler = item.getHandler()


        val stack = ItemStack(item.getMaterial())
        val meta = stack.itemMeta

        meta?.let {
            it.displayName(item.getName())
            it.lore(item.getLore())

            for (enchantment in item.getEnchantments()) {
                it.addEnchant(enchantment.key, enchantment.value, true)
            }

            for (attr in item.getAttributes()) {
                val attribute = attr.name
                it.addAttributeModifier(Attribute.valueOf(attribute), attr)
            }

            for (flag in item.getFlags()) {
                it.addItemFlags(flag)
            }

            it.persistentDataContainer.set(
                handlerKey,
                Wonderland.getAdapters().itemHandlerAdapter, itemHandler
            )

            stack.itemMeta = it
        }

        return stack
    }


}