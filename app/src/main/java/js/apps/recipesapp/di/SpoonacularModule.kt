package js.apps.recipesapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import js.apps.recipesapp.data.network.SpoonacularService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SpoonacularModule {

    @Singleton
    @Provides
    fun provideSpoonacularService(): SpoonacularService {
        return SpoonacularService.create()
    }
}