package js.apps.recipesapp.viewModel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import js.apps.recipesapp.data.RecipesRepository
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.model.edamam.Recipe
import js.apps.recipesapp.model.spoonacular.Poster
import js.apps.recipesapp.model.spoonacular.SpoonacularRecipe
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.ApiResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val recipieRepository: RecipesRepository
) : ViewModel() {

    private val _accessedBySearch = MutableStateFlow(false)
    val accessedBySearch = _accessedBySearch.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val searchUiState = MutableStateFlow(0)
    val searchUiStateFlow = searchUiState.asStateFlow()

    private val _active = MutableStateFlow(false)
    val active = _active.asStateFlow()

    private val _suggestions = MutableStateFlow(emptyList<String>().toMutableList())
    val suggestions = _suggestions.asStateFlow()

    private val _recipes = MutableStateFlow(emptyList<Result>())
    val recipes = _recipes.asStateFlow()

    private val _recipe = MutableStateFlow<Result?>(null)
    val recipe = _recipe.asStateFlow()

    private val _savedRecipes = MutableStateFlow(emptyList<Poster>())
    val savedRecipes = _savedRecipes.asStateFlow()

    val sideTag = MutableStateFlow(false)

    val isUserPremium = MutableStateFlow(false)

    val dishTag = MutableStateFlow(false)

    val dessertTag = MutableStateFlow(false)

    val breakTag = MutableStateFlow(false)

    val dinnerTag = MutableStateFlow(false)

    val mealTag = MutableStateFlow(false)

    private val _servingRange = MutableStateFlow(1f..50f)
    val servingRange = _servingRange.asStateFlow()

    private val _maxReadyMin = MutableStateFlow(200f)
    val maxReadyMin = _maxReadyMin.asStateFlow()

    private val _type = MutableStateFlow("")
    val type = _type.asStateFlow()

    private val _tags = MutableStateFlow(mutableListOf(""))
    val tags = _tags.asStateFlow()

    private val _loginState : MutableStateFlow<ApiResults<List<Result>>> = MutableStateFlow(ApiResults.Finished)
    val loginState : StateFlow<ApiResults<List<Result>>> = _loginState.asStateFlow()

    private val _searchState : MutableStateFlow<ApiResults<Result?>> = MutableStateFlow(ApiResults.Finished)
    val searchState : StateFlow<ApiResults<Result?>> = _searchState.asStateFlow()


    init {

        viewModelScope.launch {
            _suggestions.update { recipieRepository.getAllSearch().map { it.searchTerm }.toMutableList().asReversed() }
        }



    }

    fun setAccessedBySearch(accessed: Boolean) {
        _accessedBySearch.update { accessed }
    }

    fun setQuery(query: String) {
        _query.value = query
    }


    fun setSearchUiState(state: Int) {
        searchUiState.update { state }
    }


    fun setMaxServings(servingsRange: ClosedFloatingPointRange<Float>) {
        _servingRange.update { servingsRange }
    }

    fun setMaxReadyMin(maxReadyMin: Float) {
        _maxReadyMin.update { maxReadyMin }
    }

    fun setActive() {
        _active.update { !it }
    }

    fun addTag(tag:String){
        Log.w(ContentValues.TAG, tag)
        _type.value = tag
    }

    fun removeTag(){
        _type.value = ""
    }

    fun searchRecipes(){
        Log.w(ContentValues.TAG, _type.value)
        viewModelScope.launch {
            if(_type.value.isNotBlank()) {
                recipieRepository.searchRecipeByMeal(
                    query = query.value,
                    minServings = _servingRange.value.start.toInt(),
                    maxServings = _servingRange.value.endInclusive.toInt(),
                    maxReadyTime = _maxReadyMin.value.toInt(),
                    mealType = _type.value
                ).onEach { signUpState ->
                    _loginState.update {
                        signUpState

                    }
                }.launchIn(viewModelScope)
            }else{
                recipieRepository.searchRecipeFull(
                    query = query.value,
                    minServings = _servingRange.value.start.toInt(),
                    maxServings = _servingRange.value.endInclusive.toInt(),
                    maxReadyTime = _maxReadyMin.value.toInt()
                ).onEach { signUpState ->
                        _loginState.update {
                            signUpState

                        }
                    }.launchIn(viewModelScope)
            }
        }
    }
    fun setNewSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.value.isNotBlank()) {
                recipieRepository.insertSearch(query.value)
                _suggestions.value.add(0, query.value)
            }
        }
    }

    fun deleteAllSearchs() {
        viewModelScope.launch(Dispatchers.IO) {
            recipieRepository.deleteAllSearch()
            _suggestions.update { emptyList<String>().toMutableList() }
        }
    }

    fun setRecipe(recipe: Result?) {
    _recipe.update {
        recipe
    }

    }

    fun deleteRecipe() {
        viewModelScope.launch {
            recipieRepository.deleteSavedRecipes(recipe.value!!.id)
        }
    }

    fun saveRecipe() {
        viewModelScope.launch {
            recipieRepository.insertSavedRecipes(Poster(id = recipe.value!!.id,
                image = recipe.value!!.image, title =  recipe.value!!.title))
        }
    }

    fun getSavedRecipes() {
        viewModelScope.launch {
            _savedRecipes.update {
                recipieRepository.getAllSavedRecipes()
            }
        }
    }

    fun downloadRecipe(result: Result){
        viewModelScope.launch {
            recipieRepository.insertDownloads(result.toDownload())
        }
    }

    fun setRecipeById(id: Int) {

        viewModelScope.launch {
            recipieRepository.searchActualRecipe(id).onEach { flow ->
                _searchState.update {
                    flow
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteSaved(id: Int) {
        viewModelScope.launch {
            recipieRepository.deleteSavedRecipes(id)
            getSavedRecipes()
        }
    }

    fun containsElement(currentId: Int): Boolean {
        return savedRecipes.value.any { it.id == currentId }
    }

    fun clearTags() {
        sideTag.value = false
        dishTag.value = false
        dessertTag.value = false
        breakTag.value = false
        dinnerTag.value = false
        mealTag.value = false
    }

    fun setRecipes(data: List<Result>) {
        _recipes.value = data
    }

    fun resetState() {
        _loginState.update { ApiResults.Finished }
    }

    fun makeUserPremium(){
        viewModelScope.launch {
            isUserPremium.update { true }
        }
    }

    fun resetSearchState() {
        _searchState.update { ApiResults.Finished }
    }


}