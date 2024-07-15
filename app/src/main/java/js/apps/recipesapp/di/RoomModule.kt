package js.apps.recipesapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import js.apps.recipesapp.data.RecipeRepoImpl
import js.apps.recipesapp.data.RecipesRepository
import js.apps.recipesapp.data.local.RecipeDao
import js.apps.recipesapp.data.local.RecipeDatabase
import js.apps.recipesapp.data.local.SearchDao
import js.apps.recipesapp.data.network.SpoonacularService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRecipeDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "recipeDB")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideRecipeDao(db: RecipeDatabase) = db.getRecipeDao()

    @Provides
    fun provideRoomToRepository(recipeDao: RecipeDao, searchDao: SearchDao, spoonacularService: SpoonacularService): RecipesRepository {
        return RecipeRepoImpl(
            recipeDao,
            searchDao,
            spoonacularService
        )
    }

    @Singleton
    @Provides
    fun provideSearchDao(db: RecipeDatabase) = db.getSearchDao()

}
