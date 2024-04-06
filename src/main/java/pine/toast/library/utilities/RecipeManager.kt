package pine.toast.library.utilities

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import pine.toast.library.Wonderland
import java.util.*
import java.util.logging.Level


@Suppress("unused")
object RecipeManager {

    private val recipes: MutableMap<Recipe, NamespacedKey> = mutableMapOf()

    init {
        recipes.forEach { (recipe, key) ->
             if (Wonderland.getPlugin().server.addRecipe(recipe)) {
                 Wonderland.getPlugin().logger.log(Level.INFO, "Added recipe: $key")
             } else {
                 Wonderland.getPlugin().logger.log(Level.WARNING, "Failed to add recipe: $key")
             }
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

    fun createRecipe(recipeShape: RecipeShape, result: ItemStack, keyValue: UUID = UUID.randomUUID()): Pair<ShapedRecipe, NamespacedKey> {
        val key = NamespacedKey(Wonderland.getPlugin(), keyValue.toString())
        val shapedRecipe = ShapedRecipe(key, result)
        val charSlots = recipeShape.assignSlotToChar()

        shapedRecipe.shape(
            "ABC",
            "DEF",
            "GHI"
        )

        shapedRecipe.setIngredient('A', charSlots['A'] ?: Material.AIR)
        shapedRecipe.setIngredient('B', charSlots['B'] ?: Material.AIR)
        shapedRecipe.setIngredient('C', charSlots['C'] ?: Material.AIR)
        shapedRecipe.setIngredient('D', charSlots['D'] ?: Material.AIR)
        shapedRecipe.setIngredient('E', charSlots['E'] ?: Material.AIR)
        shapedRecipe.setIngredient('F', charSlots['F'] ?: Material.AIR)
        shapedRecipe.setIngredient('G', charSlots['G'] ?: Material.AIR)
        shapedRecipe.setIngredient('H', charSlots['H'] ?: Material.AIR)
        shapedRecipe.setIngredient('I', charSlots['I'] ?: Material.AIR)

        recipes[shapedRecipe] = key
        Wonderland.getPlugin().server.addRecipe(shapedRecipe)
        return Pair(shapedRecipe, key)

    }

}

data class RecipeShape(
    val slot1: Material?,
    val slot2: Material?,
    val slot3: Material?,

    val slot4: Material?,
    val slot5: Material?,
    val slot6: Material?,

    val slot7: Material?,
    val slot8: Material?,
    val slot9: Material?,

    ) {

    fun assignSlotToChar(): Map<Char, Material?> {

        val chatMaterialMap: MutableMap<Char, Material?> = mutableMapOf()

        chatMaterialMap['A'] = slot1
        chatMaterialMap['B'] = slot2
        chatMaterialMap['C'] = slot3

        chatMaterialMap['D'] = slot4
        chatMaterialMap['E'] = slot5
        chatMaterialMap['F'] = slot6

        chatMaterialMap['G'] = slot7
        chatMaterialMap['H'] = slot8
        chatMaterialMap['I'] = slot9

        return chatMaterialMap

    }

}
