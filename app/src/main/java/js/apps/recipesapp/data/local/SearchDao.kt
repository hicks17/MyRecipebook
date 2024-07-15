package js.apps.recipesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import js.apps.recipesapp.model.SavedEntity
import js.apps.recipesapp.model.SuggestedSearchEntity

@Dao
interface SearchDao {

    @Insert
    fun addSearch(search: SuggestedSearchEntity)

    @Query("SELECT * FROM suggested_searches")
    fun getAllSearchs(): List<SuggestedSearchEntity>

    @Query("DELETE FROM suggested_searches")
    fun deleteAllSearchs()

    @Query("DELETE FROM saved_recipes WHERE id = :id")
    suspend fun deleteSaved(id: Int)

    @Insert
    suspend fun addSaved(saved: SavedEntity)

    @Query("SELECT * FROM saved_recipes")
    suspend fun getAllSaved(): List<SavedEntity>


}