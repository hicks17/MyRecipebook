package js.apps.recipesapp.model.edamam

data class EdamamResponse(
    val _links: Links,
    val count: Int,
    val from: Int,
    val hits: List<Hit>,
    val to: Int
)