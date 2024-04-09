package pine.toast.library.enchants


import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.Wonderland
import pine.toast.library.WonderlandKeys
import pine.toast.library.utilities.WonderlandColors
import java.util.logging.Level


@Suppress("unused", "NAME_SHADOWING")
object EnchantmentManager : Listener {

    private val allEnchantments: MutableMap<String, WLEnchant> = mutableMapOf()

    fun registerEnchantment(enchant: WLEnchant) {
        allEnchantments[enchant.name.replace(" ", ".")] = enchant
    }


    fun createEnchantBook(enchantName: String): ItemStack {
        val enchantName = enchantName.replace(" ", ".")
        val enchantment = allEnchantments[enchantName] ?: throw IllegalArgumentException("$enchantName is not an enchantment")

        val book = ItemStack(Material.ENCHANTED_BOOK)
        val bookMeta = book.itemMeta
        bookMeta.displayName(Component.text(enchantment.name))

        bookMeta.persistentDataContainer.set(WonderlandKeys.ENCHANTMENT_STORAGE, PersistentDataType.STRING, enchantName)
        bookMeta.persistentDataContainer.set(WonderlandKeys.ENCHANT_LEVEL, PersistentDataType.INTEGER, enchantment.level)
        bookMeta.persistentDataContainer.set(WonderlandKeys.ENCHANTMENT_BOOK, PersistentDataType.BOOLEAN, true)

        val lore = mutableListOf<Component>()

        lore.add(Component.text(WonderlandColors.YELLOW + "Enchantment Level: " + enchantment.level))

        val enchTargets = enchantment.targets
        val enchTargetNames = mutableListOf<String>()

        for (target in enchTargets) {
            enchTargetNames.add(target.name)
        }

        lore.add(Component.text("Targets: " + enchTargetNames.joinToString(", ")))

        lore.add(Component.text("Drag this onto the item you want to enchant!"))

        bookMeta.lore(lore)

        book.itemMeta = bookMeta

        return book

    }


    fun intToRoman(num: Int): String {
        val romanUnits = arrayOf("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX")
        val romanTens = arrayOf("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC")

        val units = num % 10
        val tens = (num / 10) % 10

        return romanTens[tens] + romanUnits[units]
    }

    fun romanToInt(roman: String): Int {
        val romanNumerals = mapOf(
            'I' to 1, 'V' to 5, 'X' to 10, 'L' to 50,
            'C' to 100, 'D' to 500, 'M' to 1000
        )

        var result = 0
        var prevValue = 0

        for (i in roman.indices) {
            val currentValue = romanNumerals[roman[i]] ?: 0

            result += if (currentValue > prevValue) {
                currentValue - 2 * prevValue
            } else {
                currentValue
            }

            prevValue = currentValue
        }

        return result
    }


    private fun bookCheck(book: ItemStack): Boolean {
        val bookMeta = book.itemMeta ?: return false
        val storage = bookMeta.persistentDataContainer
        return storage.has(WonderlandKeys.ENCHANTMENT_BOOK)
    }


    /**
     * Returns the enchantment storage if there is one
     */
    private fun checkMainHandEnchantmentStorage(player: Player): EnchantmentStorage? {
        if (bookCheck(player.inventory.itemInMainHand)) return null
        val mainHand = player.inventory.itemInMainHand
        if (mainHand.type == Material.AIR) return null
        val mainMeta = mainHand.itemMeta
        val storage = mainMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkOffHandEnchantmentStorage(player: Player): EnchantmentStorage? {
        if (bookCheck(player.inventory.itemInOffHand)) return null
        val offHand = player.inventory.itemInOffHand
        if (offHand.type == Material.AIR) return null
        val offMeta = offHand.itemMeta
        val storage = offMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkFeetEnchantmentStorage(player: Player): EnchantmentStorage? {

        val feet = player.inventory.boots ?: return null
        val feetMeta = feet.itemMeta
        val storage = feetMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkPantsEnchantmentStorage(player: Player): EnchantmentStorage? {

        val pants = player.inventory.leggings ?: return null
        val pantsMeta = pants.itemMeta
        val storage = pantsMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkShirtEnchantmentStorage(player: Player): EnchantmentStorage? {

        val shirt = player.inventory.chestplate ?: return null
        val shirtMeta = shirt.itemMeta
        val storage = shirtMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkHelmetEnchantmentStorage(player: Player): EnchantmentStorage? {

        val helmet = player.inventory.helmet ?: return null
        val helmetMeta = helmet.itemMeta
        val storage = helmetMeta.persistentDataContainer

        if (!storage.has(WonderlandKeys.ENCHANTMENT_STORAGE)) return null

        val internalEnchStorage = storage.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)!!
        return internalEnchStorage
    }

    private fun checkEquipmentEnchantmentStorage(player: Player): List<EnchantmentStorage>? {

        val mainHand = player.inventory.itemInMainHand
        if (bookCheck(mainHand)) return null
        val offHand = player.inventory.itemInOffHand
        val feet = player.inventory.boots
        val pants = player.inventory.leggings
        val shirt = player.inventory.chestplate
        val helmet = player.inventory.helmet

        if (mainHand.type == Material.AIR && offHand.type == Material.AIR
            && feet == null && pants == null && shirt == null && helmet == null ) return null

        val list = mutableListOf<EnchantmentStorage>()

        val mainHandStorage = checkMainHandEnchantmentStorage(player)
        if (mainHandStorage != null) list.add(mainHandStorage)

        val offHandStorage = checkOffHandEnchantmentStorage(player)
        if (offHandStorage != null) list.add(offHandStorage)

        val feetStorage = checkFeetEnchantmentStorage(player)
        if (feetStorage != null) list.add(feetStorage)

        val pantsStorage = checkPantsEnchantmentStorage(player)
        if (pantsStorage != null) list.add(pantsStorage)

        val shirtStorage = checkShirtEnchantmentStorage(player)
        if (shirtStorage != null) list.add(shirtStorage)

        val helmetStorage = checkHelmetEnchantmentStorage(player)
        if (helmetStorage != null) list.add(helmetStorage)

        return list


    }



    @EventHandler(priority = EventPriority.HIGHEST)
    fun listenForEnchantApply(event: InventoryClickEvent) {
        val book = event.cursor
        if (book.type != Material.ENCHANTED_BOOK) return

        val item = event.currentItem ?: return  // Return if there's no item to enchant.
        if (item.type == Material.AIR) return   // Return if the item slot is empty.

        val bookMeta = book.itemMeta ?: return
        val bookStorage = bookMeta.persistentDataContainer
        val enchantName = bookStorage.get(WonderlandKeys.ENCHANTMENT_STORAGE, PersistentDataType.STRING) ?: return  // Return if there's no enchantment name stored.

        // Check if the enchantment exists in the map before attempting to apply it.
        val enchantment = allEnchantments[enchantName] ?: return  // Return if the enchantment does not exist.

        event.isCancelled = true

        // This includes any necessary checks for item compatibility.
        enchantment.applyEnchant(item)

        book.amount -= 1

        // Update the player's inventory to reflect changes.
        event.whoClicked.inventory.setItem(event.slot, item)  // Ensure the enchanted item is updated in the inventory.
    }





    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player

        val internalEnchStorage = checkMainHandEnchantmentStorage(player) ?: return
        val enchants = internalEnchStorage.enchants

        for (enchant in enchants) {
            if (!allEnchantments.containsKey(enchant.key)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Error: Enchantment ${enchant.key} does not exist. Skipping...")
                continue
            } else{
                allEnchantments[enchant.key]!!.onHit(player, enchant.value, event.entity)
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        val internalEnchStorage = checkMainHandEnchantmentStorage(player) ?: return
        val enchants = internalEnchStorage.enchants

        for (enchant in enchants) {
            if (!allEnchantments.containsKey(enchant.key)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Error: Enchantment ${enchant.key} does not exist. Skipping...")
                continue
            } else{
                allEnchantments[enchant.key]!!.onBlockBreak(player, enchant.value, event.block)
            }
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        val player = event.player
        val enchantStorage: List<EnchantmentStorage> = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        enchantStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onItemConsume(player, it.value, event.item)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onPlayerMove(player, it.value)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onItemDrop(player, it.value, event.itemDrop.itemStack)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player
        val killer = event.entity.killer
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onDeath(player, it.value, killer)
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player
        val internalEnchStorage = checkMainHandEnchantmentStorage(player) ?: return
        val enchants = internalEnchStorage.enchants
        for (enchant in enchants) {
            if (!allEnchantments.containsKey(enchant.key)) {
                Wonderland.getPlugin().logger.log(Level.SEVERE, "Error: Enchantment ${enchant.key} does not exist. Skipping...")
                continue
            } else{
                allEnchantments[enchant.key]!!.onProjectileHit(player, enchant.value, event.entity, event.hitBlock, event.hitEntity)
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onExpChange(event: PlayerExpChangeEvent) {
        val player = event.player
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onExpGain(player, it.value, event.amount)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerHeal(event: EntityRegainHealthEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onHeal(player, it.value, event.amount)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerTakeDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onDamageTake(player, it.value, event.cause, event.damage)
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemUse(event: PlayerInteractEvent) {
        val player = event.player
        val internalEnchStorage = checkMainHandEnchantmentStorage(player) ?: return
        val clickedBlock = event.clickedBlock
        val enchants = internalEnchStorage.enchants
        for (enchant in enchants) {
            if (!allEnchantments.containsKey(enchant.key)) {
                Wonderland.getPlugin().logger.log(
                    Level.SEVERE,
                    "Error: Enchantment ${enchant.key} does not exist. Skipping..."
                )
                continue
            } else {
                allEnchantments[enchant.key]!!.onItemUse(player, enchant.value, clickedBlock, event.action)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerKill(event: EntityDeathEvent) {
        val player = event.entity.killer ?: return
        val killedEntity = event.entity
        val internalEnchStorage = checkEquipmentEnchantmentStorage(player) ?: return
        val enchantsCollection: MutableCollection<MutableMap<String, Int>> = mutableListOf()
        internalEnchStorage.forEach { enchantsCollection.add(it.enchants) }
        val enchants: MutableMap<String, Int> = mutableMapOf()
        enchantsCollection.forEach { enchants.putAll(it) }

        enchants.forEach {
            if (!allEnchantments.containsKey(it.key)) return@forEach
            allEnchantments[it.key]!!.onKill(player, it.value, killedEntity)
        }


    }


}



