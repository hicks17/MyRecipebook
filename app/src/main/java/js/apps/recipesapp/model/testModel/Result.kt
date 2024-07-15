package js.apps.recipesapp.model.testModel

import js.apps.recipesapp.model.ResultEntity

data class Result(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val author: String,
    val cheap: Boolean,
    val cookingMinutes: Int,
    val creditsText: String,
    val cuisines: List<String>,
    val dairyFree: Boolean,
    val diets: List<String>,
    val dishTypes: List<String>,
    val extendedIngredients: List<ExtendedIngredient>,
    val gaps: String,
    val glutenFree: Boolean,
    val healthScore: Int,
    val id: Int,
    val image: String,
    val imageType: String,
    val license: String,
    val likes: Int,
    val lowFodmap: Boolean,
    val missedIngredientCount: Int,
    val missedIngredients: List<MissedIngredient>,
    val occasions: List<String>,
    val preparationMinutes: Int,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String,
    val summary: String,
    val sustainable: Boolean,
    val title: String,
    val unusedIngredients: List<Any>,
    val usedIngredientCount: Int,
    val usedIngredients: List<Any>,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int
) {
    fun toDownload(): ResultEntity {
        var ingredientString = ""
        extendedIngredients.forEach {
            ingredientString = ingredientString.plus("${it.original} \n")
        }
        var instructionString = ""
        analyzedInstructions[0].steps.forEach {
            instructionString = instructionString.plus("${it.number}: ${it.step} \n")
        }
        return ResultEntity(
            id = id,
            title = title,
            image = image,
            extendedIngredients = ingredientString,
            analyzedInstructions = instructionString,
            vegan = vegan,
            vegetarian = vegetarian,
            cheap = cheap,
            glutenFree = glutenFree,
            dairyFree = dairyFree,
            veryHealthy = veryHealthy,
            cookingMinutes = cookingMinutes,
            readyInMinutes = readyInMinutes,
            servings = servings,
            pricePerServing = pricePerServing,
            dishTypes = dishTypes.toString(),
            imageType = imageType,
            preparationMinutes = preparationMinutes,
            sourceUrl = sourceUrl,
            sourceName = sourceName,
            healthScore = healthScore,
            veryPopular = veryPopular
        )
    }
}