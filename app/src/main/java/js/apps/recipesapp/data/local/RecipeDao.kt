package js.apps.recipesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import js.apps.recipesapp.model.RecipeEntity
import js.apps.recipesapp.model.ResultEntity

@Dao
interface RecipeDao {

    @Insert
    suspend fun addRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes():List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE isFav LIKE :liked")
    suspend fun getAllFavRecipes(liked:Boolean = true):List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE porcion > :q")
    suspend fun getAllRecipesByPortion(q:Int):List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE porcion < :q")
    suspend fun getAllRecipesByPortionLess(q:Int):List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE porcion LIKE :q")
    suspend fun getAllRecipesByPortionSpecific(q:Int):List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE porcion BETWEEN :min AND :max")
    suspend fun getAllRecipesByPortionSpecific(min:Int, max:Int):List<RecipeEntity>

    @Query("SELECT * FROM recipes ORDER BY id DESC LIMIT 1")
    suspend fun obtenerUltimoRegistro(): RecipeEntity

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipeEntity: RecipeEntity)

    @Query("UPDATE recipes SET isFav = :isFav WHERE id = :ide ")
    suspend fun changeFav(ide:Int, isFav: Boolean)

    @Insert
    suspend fun addDownloadRecipe(recipeEntity: ResultEntity)

    @Query("SELECT * FROM download_recipes")
    suspend fun getAllDownloadRecipes():List<ResultEntity>

    @Delete
    suspend fun deleteDownloadRecipe(recipeEntity: ResultEntity)


}