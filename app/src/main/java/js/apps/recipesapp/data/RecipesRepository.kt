package js.apps.recipesapp.data

import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.model.SuggestedSearchEntity
import js.apps.recipesapp.model.spoonacular.Poster
import js.apps.recipesapp.model.spoonacular.SpoonacularRecipe
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.ApiResults
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RecipesRepository {

    suspend fun getLocalRecipes():List<Recipe>

    suspend fun getLocalFavRecipes():List<Recipe>

    suspend fun getLastRecipe():Recipe

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun addNewRecipe(recipe: Recipe)

    suspend fun updateFavorite(boolean: Boolean, id:Int)

    suspend fun insertSearch(search:String)

    suspend fun deleteAllSearch()

    suspend fun getAllSearch():List<SuggestedSearchEntity>

    suspend fun searchRecipesByNetwork(query:String):List<Result>

    suspend fun searchActualRecipe(id:Int):Flow<ApiResults<Result?>>

    suspend fun getAllSavedRecipes():List<Poster>

    suspend fun deleteSavedRecipes(id:Int)

    suspend fun insertSavedRecipes(poster:Poster)

    suspend fun getAllDownloads():List<ResultEntity>

    suspend fun deleteDownloads(resultEntity: ResultEntity)

    suspend fun insertDownloads(resultEntity: ResultEntity)

    suspend fun searchRecipeFull(query: String, maxReadyTime:Int,
                                 minServings:Int, maxServings:Int): Flow<ApiResults<List<Result>>>

    suspend fun searchRecipeByMeal(query: String, maxReadyTime:Int,
                                 minServings:Int, maxServings:Int, mealType:String): Flow<ApiResults<List<Result>>>
}