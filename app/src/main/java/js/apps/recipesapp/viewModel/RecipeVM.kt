package js.apps.recipesapp.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import js.apps.recipesapp.data.RecipesRepository
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.ResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeVM @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _downloadRecipe = MutableStateFlow<ResultEntity?>(null)
    val downloadRecipe = _downloadRecipe.asStateFlow()

    private val _recipeUiStateFlow = MutableStateFlow(0)
    val recipeUiState = _recipeUiStateFlow.asStateFlow()

    private val _recipeList = MutableStateFlow(emptyList<Recipe>().toMutableList())
    val recipeList = _recipeList.asStateFlow()

    private val _filteredList = MutableStateFlow(emptyList<Recipe>().toMutableList())
    val filteredList = _filteredList.asStateFlow()

    private val _totalList = MutableStateFlow(emptyList<Recipe>())
    val totalList = _totalList.asStateFlow()

    val query = MutableStateFlow("")

    private val _recipeFavList = MutableStateFlow(emptyList<Recipe>())
    val recipeFavList = _recipeFavList.asStateFlow()

    private val _recipe = MutableStateFlow<Recipe>(Recipe(null, "", "", "", "", "", 0, null, 0, false))
    val recipe = _recipe.asStateFlow()

    private val _isFav = MutableStateFlow(false)
    val isFav = _isFav.asStateFlow()

    private val _isDownloadSelected = MutableStateFlow(false)
    val isDownloadSelected = _isDownloadSelected.asStateFlow()

    private val _downloadedRecipes = MutableStateFlow(emptyList<ResultEntity>())
    val downloadedRecipes = _downloadedRecipes.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing = _isEditing.asStateFlow()

    var _isFavChipSelected = MutableStateFlow(false)
    val isFavChipSelected = _isFavChipSelected.asStateFlow()

    val _tags = MutableStateFlow(emptyList<String>().toMutableList())

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown = _isDialogShown.asStateFlow()

    private val _isFilterShown = MutableStateFlow(false)
    val isFilterShown = _isFilterShown.asStateFlow()


    init {
        viewModelScope.launch {
            _recipeList.update {
                repository.getLocalRecipes().toMutableList()
            }
            _recipeFavList.update {
                repository.getLocalFavRecipes().toMutableList()
            }
            _totalList.update {
                repository.getLocalRecipes()
            }
            _downloadedRecipes.update {
                repository.getAllDownloads()
            }

        }
    }

    fun setDownloadRecipe(resultEntity: ResultEntity) {
        _downloadRecipe.update {
            resultEntity
        }
    }

    fun onNewDownload(resultEntity: ResultEntity) {
        _downloadedRecipes.update {
            it + resultEntity
        }
    }

    fun setDownloadSelected(boolean: Boolean) {
        _isDownloadSelected.update { boolean }
    }

    fun setUiState(state: Int) {
        _recipeUiStateFlow.update { state }
    }

        fun setCurrentRecipe(recipe: Recipe) {
            _recipe.update {
                recipe
            }
            _isFav.update {
                recipe.isFav
            }
        }

        fun changeFav(boolean: Boolean) {
            _isFav.update {
                boolean
            }
            viewModelScope.launch {
                repository.updateFavorite(boolean, _recipe.value.id!!)
            }
        }

        fun deleteRecipe(recipeDeleted: Recipe) {
            viewModelScope.launch {
                repository.deleteRecipe(recipeDeleted)
            }
            _recipeList.update {
                (it - recipeDeleted).toMutableList()
            }
            _recipeFavList.update {
                it - recipeDeleted
            }
        }

        fun updateRecipe(recipe: Recipe){

            viewModelScope.launch {
                repository.updateRecipe(recipe)
                _recipe.update {
                    recipe
                }
            }
        }

        fun setLastRecipe(){
            viewModelScope.launch {
                val e = repository.getLastRecipe()
                Log.w(TAG, e.toString())
                _recipe.update { e }
            }
        }

        fun updateList() {
            viewModelScope.launch {
                _recipeList.update {
                    repository.getLocalRecipes().toMutableList()
                }
                _totalList.update {
                    repository.getLocalRecipes()
                }
            }
        }


        fun onClickDelete() {
            _isDialogShown.update {
                !it
            }
        }

        fun onFilterShown() {
            _isFilterShown.update {
                !it
            }
        }

        fun setFavoriteList() {
            viewModelScope.launch {
                _recipeFavList.update {
                    repository.getLocalFavRecipes().toMutableList()
                }

                _recipeList.update {
                    _recipeFavList.value.toMutableList()
                }
            }

        }

        fun onFilterSelected(tag:String){
            _tags.value.add(tag)

            _filteredList.update {
                emptyList<Recipe>().toMutableList()
            }

            checkFilters()

            _recipeList.update {
                _filteredList.value
            }
        }

        private fun checkFilters(){
                    totalList.value.forEach { recipe ->
                        if (_tags.value.isNotEmpty()) {
                            Log.w(TAG, "not empty")
                            _tags.value.forEach {
                                Log.w(TAG, "tag: $it")
                                if (recipe.tags.contains(it)) {
                                    if(!_filteredList.value.contains(recipe)){
                                        Log.w(TAG, "Se cumple")
                                        _filteredList.value.add(recipe)
                                    }

                                }
                            }
                        } else {
                            Log.w(TAG, "empty")
                            _filteredList.value = totalList.value.toMutableList()
                        }

            }
            Log.w(TAG, _filteredList.value.toString())
        }

        fun onRemovedFilter(tag: String){

            _tags.value.remove(tag)

            _filteredList.update {
                emptyList<Recipe>().toMutableList()
            }

            Log.w(TAG, "lista: ${_filteredList.value}")
            checkFilters()
            _recipeList.update {
                _filteredList.value
            }
        }

        fun searchByTitle(){
            Log.w(TAG, query.value)

            _filteredList.update {
                _totalList.value.filter { it.title.lowercase().contains(query.value) }.toMutableList()
            }
            _recipeList.update {
                _filteredList.value
            }
        }

    fun onFavClicked(fav: Boolean, recipe: Recipe) {
        updateRecipe(recipe.copy(isFav = fav))
        val currentRecipes = _totalList.value.toMutableList()

        // 2. Encontrar y modificar el elemento

        if(_isFavChipSelected.value){
            setFavoriteList()
        }

    }

    fun setDownloads() {

    }

    fun deleteDownload(download: ResultEntity) {
        viewModelScope.launch {
            repository.deleteDownloads(download)
        }
            _downloadedRecipes.update {
                it - download
            }
    }

}

