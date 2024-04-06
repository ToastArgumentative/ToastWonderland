package pine.toast.library.enchants

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import pine.toast.library.Wonderland
import pine.toast.library.WonderlandKeys
import java.util.*


data class EnchantmentStorage(var enchantmentKey: MutableList<NamespacedKey> )
data class WLEnchantProps(val name: String, val level: Int, val target: WLEnchantmentTarget, val activationType: WLEnchantmentType )

@Suppress("unused")
object EnchantmentManager { 

    private val enchantments: MutableMap<NamespacedKey, WLEnchantUse> = mutableMapOf()

    fun applyEnchantmentsToItem(enchantment: NamespacedKey, item: ItemStack) {
        val enchantmentTarget = getEnchantment(enchantment).enchantmentProps.target

        if (!enchantmentTarget.includes(item.type)) throw IllegalArgumentException("Enchantment $enchantment is not valid for item type ${item.type.name}")

        val itemMeta = item.itemMeta!!
        val enchantPDC = itemMeta.persistentDataContainer

        if (!enchantPDC.has(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter)) {
            val enchantmentStorage = EnchantmentStorage(mutableListOf(enchantment))
            enchantPDC.set(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter, enchantmentStorage)
        } else {
            val enchantmentStorage = enchantPDC.get(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter) as EnchantmentStorage
            enchantmentStorage.enchantmentKey.add(enchantment)
            enchantPDC.set(WonderlandKeys.ENCHANTMENT_STORAGE, Wonderland.getAdapters().enchantmentStorageAdapter, enchantmentStorage)
        }

        val newLore = mutableListOf<String>()

        newLore.add(enchantment.toString())

        for (loreLine in itemMeta.lore() ?: mutableListOf()) {
            val serializer = PlainTextComponentSerializer.plainText()
            val line = serializer.serialize(loreLine)
            newLore.add(line)
        }

    }

    private fun getEnchantment(key: NamespacedKey): WLEnchantUse {
        // check if the enchantment is valid I want to return a guaranteed enchantment if not throw
        return enchantments[key] ?: throw IllegalArgumentException("Enchantment $key is not a valid enchantment")
    }

    fun createNewEnchantment(enchantHandler: Class<out WLEnchantUse>): NamespacedKey {
        val instance = enchantHandler.getConstructor().newInstance()
        val key = NamespacedKey(Wonderland.getPlugin(), instance.enchantmentProps.name.lowercase(Locale.ROOT))
        addEnchantmentUse(key, instance)
        return key
    }

    fun getEnchantments(): Map<NamespacedKey, WLEnchantUse> {
        return HashMap(enchantments)
    }

    private fun removeEnchantmentUse(key: NamespacedKey) {
        enchantments.remove(key)
    }

    private fun addEnchantmentUse(key: NamespacedKey, use: WLEnchantUse) {
        enchantments[key] = use
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

            if (currentValue > prevValue) {
                result += currentValue - 2 * prevValue
            } else {
                result += currentValue
            }

            prevValue = currentValue
        }

        return result
    }



}



