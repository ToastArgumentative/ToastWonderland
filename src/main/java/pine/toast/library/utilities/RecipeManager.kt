package pine.toast.library.utilities

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import pine.toast.library.Wonderland
import java.util.*
import java.util.logging.Level


@Suppress("unused")
object RecipeManager {

    private val recipes: MutableMap<Recipe, NamespacedKey> = mutableMapOf()


    fun registerRecipes() {
        recipes.forEach { (recipe, key) ->
            val checkRecipe: Recipe? = Bukkit.getRecipe(key)
            if (checkRecipe == null) {
                if (Wonderland.getPlugin().server.addRecipe(recipe)) {
                    Wonderland.getPlugin().logger.log(Level.INFO, "Added recipe: $key")
                } else {
                    Wonderland.getPlugin().logger.log(Level.WARNING, "Failed to add recipe: $key")
                }
            } else {
                Wonderland.getPlugin().logger.log(Level.WARNING, "$key already exists skipping...")
            }
        }
    }

    fun unregisterRecipes() {
        recipes.forEach { (_, key) ->
            Wonderland.getPlugin().server.removeRecipe(key)
        }
    }

    fun getRecipes(): Map<Recipe, NamespacedKey> {
        return recipes
    }

    fun getRecipe(key: NamespacedKey): Recipe? {
        return recipes.filter { it.value == key }.map { it.key }.firstOrNull()
    }

    fun getRecipe(key: UUID): Recipe? {
        val keyString = key.toString()
        return recipes.filter { it.value.toString() == keyString }.map { it.key }.firstOrNull()
    }

    fun createRecipe(recipeShape: RecipeShape, result: ItemStack, keyValue: String = UUID.randomUUID().toString()): Pair<ShapedRecipe, NamespacedKey> {
        val key = NamespacedKey(Wonderland.getPlugin(), keyValue)
        val shapedRecipe = ShapedRecipe(key, result)
        val charSlots = recipeShape.assignSlotToChar()

        shapedRecipe.shape(
            "ABC",
            "DEF",
            "GHI"
        )

        shapedRecipe.setIngredient('A', RecipeChoice.ExactChoice(charSlots['A'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('B', RecipeChoice.ExactChoice(charSlots['B'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('C', RecipeChoice.ExactChoice(charSlots['C'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('D', RecipeChoice.ExactChoice(charSlots['D'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('E', RecipeChoice.ExactChoice(charSlots['E'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('F', RecipeChoice.ExactChoice(charSlots['F'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('G', RecipeChoice.ExactChoice(charSlots['G'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('H', RecipeChoice.ExactChoice(charSlots['H'] ?: ItemStack(Material.AIR)))
        shapedRecipe.setIngredient('I', RecipeChoice.ExactChoice(charSlots['I'] ?: ItemStack(Material.AIR)))


        recipes[shapedRecipe] = key
        Wonderland.getPlugin().server.addRecipe(shapedRecipe)
        return Pair(shapedRecipe, key)
    }


}

data class RecipeShape(
    val slot1: ItemStack?,
    val slot2: ItemStack?,
    val slot3: ItemStack?,

    val slot4: ItemStack?,
    val slot5: ItemStack?,
    val slot6: ItemStack?,

    val slot7: ItemStack?,
    val slot8: ItemStack?,
    val slot9: ItemStack?
) {

    fun assignSlotToChar(): Map<Char, ItemStack?> {

        val charItemStackMap: MutableMap<Char, ItemStack?> = mutableMapOf()

        charItemStackMap['A'] = slot1
        charItemStackMap['B'] = slot2
        charItemStackMap['C'] = slot3

        charItemStackMap['D'] = slot4
        charItemStackMap['E'] = slot5
        charItemStackMap['F'] = slot6

        charItemStackMap['G'] = slot7
        charItemStackMap['H'] = slot8
        charItemStackMap['I'] = slot9

        return charItemStackMap
    }
}

