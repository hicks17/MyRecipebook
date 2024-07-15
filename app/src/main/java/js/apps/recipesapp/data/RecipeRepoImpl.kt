package js.apps.recipesapp.data

import js.apps.recipesapp.data.local.RecipeDao
import js.apps.recipesapp.data.local.SearchDao
import js.apps.recipesapp.data.network.SpoonacularService
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.RecipeEntity
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.model.SuggestedSearchEntity
import js.apps.recipesapp.model.spoonacular.Poster
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.ApiResults
import js.apps.recipesapp.utils.ApiResults.Loading
import js.apps.recipesapp.utils.ApiResults.Error
import js.apps.recipesapp.utils.toEntity
import js.apps.recipesapp.utils.toPoster
import js.apps.recipesapp.utils.toRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecipeRepoImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val searchDao: SearchDao,
    private val spoonacularApi: SpoonacularService
): RecipesRepository {
    override suspend fun getLocalRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO){
            recipeDao.getAllRecipes().map { it.toRecipe() }
        }

    }

    override suspend fun getLocalFavRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO){
            recipeDao.getAllFavRecipes().map { it.toRecipe() }
        }
    }

    override suspend fun getLastRecipe(): Recipe {
        return withContext(Dispatchers.IO){
            recipeDao.obtenerUltimoRegistro().toRecipe()
        }
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.toEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }

    override suspend fun addNewRecipe(recipe: Recipe) {
        recipeDao.addRecipe(RecipeEntity(
                title = recipe.title,
            desc = recipe.desc,
            personas = recipe.personas,
            ingredientes = recipe.ingredientes,
            image = recipe.image,
            instrucciones = recipe.instrucciones,
            tiempo = recipe.tiempo,
            isFav = recipe.isFav,
            tags = recipe.tags
        ))
    }

    override suspend fun updateFavorite(boolean: Boolean, id: Int) {
        recipeDao.changeFav(id, boolean)
    }

    override suspend fun insertSearch(search: String) {
        searchDao.addSearch(SuggestedSearchEntity( searchTerm = search))
    }

    override suspend fun deleteAllSearch() {
        searchDao.deleteAllSearchs()
    }

    override suspend fun getAllSearch(): List<SuggestedSearchEntity> {
        return withContext(Dispatchers.IO) {
            searchDao.getAllSearchs()
        }
    }

    override suspend fun searchRecipesByNetwork(query: String): List<Result> {
        return withContext(Dispatchers.IO){
            val response = spoonacularApi.searchRecipeByQuery(query)
            response.results
        }
    }

    override suspend fun searchActualRecipe(id: Int) = flow {
        emit(Loading)
        try {
            val response: Result?

            val petition = spoonacularApi.searchRecipeById(id)
            if (petition.isSuccessful){
                response = petition.body()
                emit(ApiResults.Success(response))
            }else{
                emit(ApiResults.NoSuccess(petition.message(), data = null))
            }
        }catch (e:Exception){
            emit(Error(e))
        }

    }

    override suspend fun getAllSavedRecipes(): List<Poster> {
        return withContext(Dispatchers.IO) {
            searchDao.getAllSaved().map { it.toPoster() }
        }
    }

    override suspend fun deleteSavedRecipes(id: Int) {
        searchDao.deleteSaved(id)
    }

    override suspend fun insertSavedRecipes(poster: Poster) {
        searchDao.addSaved(poster.toEntity())
    }

    override suspend fun getAllDownloads(): List<ResultEntity> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllDownloadRecipes()
        }
    }

    override suspend fun deleteDownloads(resultEntity: ResultEntity) {
        recipeDao.deleteDownloadRecipe(resultEntity)
    }

    override suspend fun insertDownloads(resultEntity: ResultEntity) {
        recipeDao.addDownloadRecipe(resultEntity)
    }

    override suspend fun searchRecipeFull(
        query: String,
        maxReadyTime: Int,
        minServings: Int,
        maxServings: Int
    ): Flow<ApiResults<List<Result>>> = flow {
       emit(Loading)
        try {
            val response: List<Result>

            val petition = spoonacularApi.searchRecipeFull(
                query = query,
                tiempo = maxReadyTime,
                minServings = minServings,
                maxServings = maxServings
            )
            if (petition.isSuccessful){
                response = petition.body()?.results ?: emptyList()
                emit(ApiResults.Success(response))
            }else{
                emit(ApiResults.NoSuccess(petition.message(), data = emptyList<Result>()))
            }
        }catch (e:Exception){
            emit(Error(e))
        }
    }

    override suspend fun searchRecipeByMeal(
        query: String,
        maxReadyTime: Int,
        minServings: Int,
        maxServings: Int,
        mealType: String
    ): Flow<ApiResults<List<Result>>> = flow {
        emit(Loading)
        try {
            val response: List<Result>

            val petition = spoonacularApi.searchRecipeByMealType(
                query = query,
                tiempo = maxReadyTime,
                minServings = minServings,
                maxServings = maxServings,
                mealType = mealType
            )
            if (petition.isSuccessful){
                response = petition.body()?.results ?: emptyList()
                emit(ApiResults.Success(response))
            }else{
                emit(ApiResults.NoSuccess(petition.message(), data = emptyList<Result>()))
            }
        }catch (e:Exception){
            emit(Error(e))
        }
    }
}