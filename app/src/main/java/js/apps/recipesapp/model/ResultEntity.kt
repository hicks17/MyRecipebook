package js.apps.recipesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import js.apps.recipesapp.model.testModel.AnalyzedInstruction
import js.apps.recipesapp.model.testModel.ExtendedIngredient
import js.apps.recipesapp.model.testModel.MissedIngredient

@Entity(tableName = "download_recipes")
data class ResultEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "instructions") val analyzedInstructions: String,
    @ColumnInfo(name = "isCheap")val cheap: Boolean,
    @ColumnInfo(name = "cookingMinutes") val cookingMinutes: Int,
    @ColumnInfo(name = "dairyFree") val dairyFree: Boolean,
    @ColumnInfo(name = "dishTypes") val dishTypes: String,
    @ColumnInfo(name = "extendedIngredients")val extendedIngredients: String,
    @ColumnInfo(name = "glutenFree") val glutenFree: Boolean,
    @ColumnInfo(name = "healthScore") val healthScore: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "imageType") val imageType: String,
    @ColumnInfo(name = "preparationMinutes")val preparationMinutes: Int,
    @ColumnInfo(name = "pricePerServing") val pricePerServing: Double,
    @ColumnInfo(name = "readyInMinutes") val readyInMinutes: Int,
    @ColumnInfo(name = "servings") val servings: Int,
    @ColumnInfo(name = "sourceName") val sourceName: String,
    @ColumnInfo(name = "sourceUrl") val sourceUrl: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vegan") val vegan: Boolean,
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean,
    @ColumnInfo(name = "veryHealthy") val veryHealthy: Boolean,
    @ColumnInfo(name = "veryPopular") val veryPopular: Boolean
)
