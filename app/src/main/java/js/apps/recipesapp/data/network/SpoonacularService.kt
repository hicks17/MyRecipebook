package js.apps.recipesapp.data.network

import com.google.gson.GsonBuilder
import js.apps.recipesapp.model.edamam.EdamamResponse
import js.apps.recipesapp.model.spoonacular.SpoonacularRecipe
import js.apps.recipesapp.model.spoonacular.SpoonacularResponse
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.model.testModel.TestResponse
import js.apps.recipesapp.utils.DoubleDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface SpoonacularService {


        @GET("complexSearch/")
        suspend fun searchRecipeByQuery(
            @Query("query") query: String,
            @Query("instructionsRequired") reqInstructions: Boolean = true,
            @Query("addRecipeInformation") addInfo: Boolean = true,
            @Query("fillIngredients") fillIngredients: Boolean = true,
            @Query("apiKey") key: String = APP_KEY,
            @Query("number") number: Int = 50,
        ): TestResponse

    /*main course
    side dish
    dessert
    appetizer
    salad
    bread
    breakfast
    soup
    beverage
    sauce
    marinade
    fingerfood
    snack
    drink*/
        @GET("complexSearch/")
        suspend fun searchRecipeFull(
            @Query("query") query: String,
            @Query("instructionsRequired") reqInstructions: Boolean = true,
            @Query("addRecipeInformation") addInfo: Boolean = true,
            @Query("fillIngredients") fillIngredients: Boolean = true,
            @Query("maxReadyTime") tiempo: Int,
            @Query("minServings") minServings: Int,
            @Query("maxServings") maxServings: Int ,
            @Query("apiKey") key: String = APP_KEY,
            @Query("number") number: Int = 60,
        ): Response<TestResponse>

        @GET("complexSearch/")
        suspend fun searchRecipeByMealType(
            @Query("query") query: String = "",
            @Query("instructionsRequired") reqInstructions: Boolean = true,
            @Query("addRecipeInformation") addInfo: Boolean = true,
            @Query("fillIngredients") fillIngredients: Boolean = true,
            @Query("type") mealType: String,
            @Query("maxReadyTime") tiempo: Int,
            @Query("minServings") minServings: Int,
            @Query("maxServings") maxServings: Int,
            @Query("apiKey") key: String = APP_KEY,
            @Query("number") number: Int = 60,
        ): Response<TestResponse>


        @GET("{id}/information")
        suspend fun searchRecipeById(
            @Path("id") id: Int,
            @Query("apiKey") appId: String = APP_KEY
        ): Response<Result>

        companion object {

            private const val BASE_URL = "https://api.spoonacular.com/recipes/"
            private const val APP_KEY = "da88e149156f417ebd46f24badf7eecf"

            fun create(): SpoonacularService {
                val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

                /*val gson = GsonBuilder()
                    .registerTypeAdapter(Double::class.java, DoubleDeserializer())
                    .create()*/
                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .callTimeout(7, TimeUnit.SECONDS)
                    .build()

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SpoonacularService::class.java)
            }
        }
    }
