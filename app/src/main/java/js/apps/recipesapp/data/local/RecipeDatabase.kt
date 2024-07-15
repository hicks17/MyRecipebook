package js.apps.recipesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import js.apps.recipesapp.model.RecipeEntity
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.model.SavedEntity
import js.apps.recipesapp.model.SuggestedSearchEntity

@Database(entities = [RecipeEntity::class, SuggestedSearchEntity::class, SavedEntity::class, ResultEntity::class],
    exportSchema = false,
    version = 4)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase(){

    abstract fun getRecipeDao(): RecipeDao
    abstract fun getSearchDao(): SearchDao
}