package js.apps.recipesapp.model.edamam

data class Digest(
    val daily: Double,
    val hasRDI: Boolean,
    val label: String,
    val schemaOrgTag: String,
    val sub: List<DigestX>,
    val tag: String,
    val total: Double,
    val unit: String
)