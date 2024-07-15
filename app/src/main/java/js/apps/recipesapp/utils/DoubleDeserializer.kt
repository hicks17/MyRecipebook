package js.apps.recipesapp.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DoubleDeserializer : JsonDeserializer<Double> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double {
        return if (json != null && json.isJsonPrimitive && json.asString == "NaN") {
            Double.NaN // Or any default value you prefer
        } else {
            json?.asDouble ?: 0.0 // Handle null or non-double values
        }
    }
}