package js.apps.recipesapp.model.spoonacular

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)