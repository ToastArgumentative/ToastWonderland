package pine.toast.library.enchants

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Suppress("unused")
enum class WLEnchantmentTarget {

    ARMOR {
        override fun includes(item: Material): Boolean {
            return (ARMOR_FEET.includes(item)
                    || ARMOR_LEGS.includes(item)
                    || ARMOR_HEAD.includes(item)
                    || ARMOR_CHEST.includes(item))
        }
    },

    ARMOR_FEET {
        override fun includes(item: Material): Boolean {
            return item == Material.LEATHER_BOOTS ||
                    item == Material.CHAINMAIL_BOOTS ||
                    item == Material.IRON_BOOTS ||
                    item == Material.DIAMOND_BOOTS ||
                    item == Material.GOLDEN_BOOTS ||
                    item == Material.NETHERITE_BOOTS
        }
    },

    ARMOR_LEGS {
        override fun includes(item: Material): Boolean {
            return item == Material.LEATHER_LEGGINGS ||
                    item == Material.CHAINMAIL_LEGGINGS ||
                    item == Material.IRON_LEGGINGS ||
                    item == Material.DIAMOND_LEGGINGS ||
                    item == Material.GOLDEN_LEGGINGS ||
                    item == Material.NETHERITE_LEGGINGS
        }
    },

    ARMOR_CHEST {
        override fun includes(item: Material): Boolean {
            return item == Material.LEATHER_CHESTPLATE ||
                    item == Material.CHAINMAIL_CHESTPLATE ||
                    item == Material.IRON_CHESTPLATE ||
                    item == Material.DIAMOND_CHESTPLATE ||
                    item == Material.GOLDEN_CHESTPLATE ||
                    item == Material.NETHERITE_CHESTPLATE
        }
    },

    ARMOR_HEAD {
        override fun includes(item: Material): Boolean {
            return item == Material.LEATHER_HELMET ||
                    item == Material.CHAINMAIL_HELMET ||
                    item == Material.IRON_HELMET ||
                    item == Material.DIAMOND_HELMET ||
                    item == Material.GOLDEN_HELMET ||
                    item == Material.TURTLE_HELMET ||
                    item == Material.NETHERITE_HELMET
        }
    },

    WEAPON {
        override fun includes(item: Material): Boolean {
            return item == Material.WOODEN_SWORD ||
                    item == Material.STONE_SWORD ||
                    item == Material.IRON_SWORD ||
                    item == Material.DIAMOND_SWORD ||
                    item == Material.GOLDEN_SWORD ||
                    item == Material.NETHERITE_SWORD ||
                    item == Material.WOODEN_AXE ||
                    item == Material.STONE_AXE ||
                    item == Material.IRON_AXE ||
                    item == Material.DIAMOND_AXE ||
                    item == Material.GOLDEN_AXE ||
                    item == Material.NETHERITE_AXE
        }
    },

    TOOLS {
        override fun includes(item: Material): Boolean {
            return (SHOVEL.includes(item)
                    || PICKAXE.includes(item)
                    || AXE.includes(item)
                    || HOE.includes(item))
        }
    },

    SHOVEL {
        override fun includes(item: Material): Boolean {
            return item == Material.WOODEN_SHOVEL ||
                    item == Material.STONE_SHOVEL ||
                    item == Material.IRON_SHOVEL ||
                    item == Material.DIAMOND_SHOVEL ||
                    item == Material.GOLDEN_SHOVEL ||
                    item == Material.NETHERITE_SHOVEL
        }
    },

    PICKAXE {
        override fun includes(item: Material): Boolean {
            return item == Material.WOODEN_PICKAXE ||
                    item == Material.STONE_PICKAXE ||
                    item == Material.IRON_PICKAXE ||
                    item == Material.DIAMOND_PICKAXE ||
                    item == Material.GOLDEN_PICKAXE ||
                    item == Material.NETHERITE_PICKAXE
        }
    },

    AXE {
        override fun includes(item: Material): Boolean {
            return item == Material.WOODEN_AXE ||
                    item == Material.STONE_AXE ||
                    item == Material.IRON_AXE ||
                    item == Material.DIAMOND_AXE ||
                    item == Material.GOLDEN_AXE ||
                    item == Material.NETHERITE_AXE
        }
    },

    HOE {
        override fun includes(item: Material): Boolean {
            return item == Material.WOODEN_HOE ||
                    item == Material.STONE_HOE ||
                    item == Material.IRON_HOE ||
                    item == Material.DIAMOND_HOE ||
                    item == Material.GOLDEN_HOE ||
                    item == Material.NETHERITE_HOE
        }
    },

    BOW {
        override fun includes(item: Material): Boolean {
            return item == Material.BOW
        }
    },


    FISHING_ROD {
        override fun includes(item: Material): Boolean {
            return item == Material.FISHING_ROD
        }
    },

    BREAKABLE {
        override fun includes(item: Material): Boolean {
            return item.maxDurability > 0 && item.maxStackSize == 1
        }
    },

    WEARABLE {
        override fun includes(item: Material): Boolean {
            return (ARMOR.includes(item)
                    || ELYTRA.includes(item)) ||
                    item == Material.CARVED_PUMPKIN ||
                    item == Material.JACK_O_LANTERN ||
                    item == Material.SKELETON_SKULL ||
                    item == Material.WITHER_SKELETON_SKULL ||
                    item == Material.ZOMBIE_HEAD ||
                    item == Material.PLAYER_HEAD ||
                    item == Material.CREEPER_HEAD ||
                    item == Material.DRAGON_HEAD
        }
    },

    ELYTRA {
        override fun includes(item: Material): Boolean {
            return item == Material.ELYTRA
        }
    },

    TRIDENT {
        override fun includes(item: Material): Boolean {
            return item == Material.TRIDENT
        }
    },

    CROSSBOW {
        override fun includes(item: Material): Boolean {
            return item == Material.CROSSBOW
        }
    },

    VANISHABLE {
        override fun includes(item: Material): Boolean {
            return BREAKABLE.includes(item) || (WEARABLE.includes(
                item
            ) && item != Material.ELYTRA) || item == Material.COMPASS
        }
    };

    abstract fun includes(item: Material): Boolean

    fun includes(item: ItemStack): Boolean {
        return includes(item.type)
    }
}
