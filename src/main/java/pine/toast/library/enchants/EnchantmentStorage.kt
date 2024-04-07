package pine.toast.library.enchants

import java.io.Serializable

data class EnchantmentStorage(
    val enchants: MutableMap<String, Int>
) : Serializable
