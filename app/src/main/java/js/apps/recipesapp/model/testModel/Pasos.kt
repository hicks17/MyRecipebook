package js.apps.recipesapp.model.testModel

data class Pasos(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)