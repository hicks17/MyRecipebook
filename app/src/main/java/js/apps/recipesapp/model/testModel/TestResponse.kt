package js.apps.recipesapp.model.testModel

data class TestResponse(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)