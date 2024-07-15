package js.apps.recipesapp.model.spoonacular

data class Poster(
    val id: Int,
    val image: String,
    val roomId: Int? = null,
    val title: String
)