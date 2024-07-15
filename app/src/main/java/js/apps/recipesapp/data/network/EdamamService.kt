package js.apps.recipesapp.data.network

import com.google.gson.GsonBuilder
import js.apps.recipesapp.model.edamam.EdamamResponse
import js.apps.recipesapp.utils.DoubleDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamService {

    @GET("api/recipes/v2/")
    suspend fun searchRecipe(
        @Query("q") query: String,
        @Query("type") type: String = "public",
        @Query("app_key") perPage: String = APP_KEY,
        @Query("app_id") appId: String = APP_ID
    ): EdamamResponse


    suspend fun searchRecipeByMealType(
        @Query("q") query: String,
        @Query("type") type: String = "public",
        @Query("mealType") meals: List<String>,
        @Query("app_key") perPage: String = APP_KEY,
        @Query("app_id") appId: String = APP_ID
    ): EdamamResponse

    companion object {

        private const val BASE_URL = "https://api.edamam.com/"
        private const val APP_ID = "e4016c86"
        private const val APP_KEY = "ab83177411445576b19f19e045418686"

        fun create(): EdamamService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val gson = GsonBuilder()
                .registerTypeAdapter(Double::class.java, DoubleDeserializer())
                .create()
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(EdamamService::class.java)
        }
    }
}