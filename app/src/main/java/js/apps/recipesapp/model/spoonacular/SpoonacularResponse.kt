package js.apps.recipesapp.model.spoonacular

data class SpoonacularResponse(
    val number: Int,
    val offset: Int,
    val results: List<Poster>,
    val totalResults: Int
)