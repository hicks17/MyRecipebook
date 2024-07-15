package js.apps.recipesapp.viewModel

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import js.apps.recipesapp.data.RecipesRepository
import js.apps.recipesapp.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeVM @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {


    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown = _isDialogShown.asStateFlow()

    private val _isFav = MutableStateFlow(false)
    val isFav = _isFav.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    val sideTag = MutableStateFlow(false)

    val dishTag = MutableStateFlow(false)

    val dessertTag = MutableStateFlow(false)

    val breakTag = MutableStateFlow(false)

    val dinnerTag = MutableStateFlow(false)

    val mealTag = MutableStateFlow(false)

    private val _descripcion = MutableStateFlow("")
    val descripcion = _descripcion.asStateFlow()

    private val _ingredientes = MutableStateFlow("")
    val ingredientes = _ingredientes.asStateFlow()

    private val _instrucciones = MutableStateFlow("")
    val instrucciones = _instrucciones.asStateFlow()

    private val _tiempo = MutableStateFlow(0)
    val tiempo = _tiempo.asStateFlow()

    private val _personas = MutableStateFlow(0)
    val personas = _personas.asStateFlow()

    private val _tags = MutableStateFlow(mutableListOf(""))
    val tags = _tags.asStateFlow()

    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe = _recipe.asStateFlow()

    fun changeTitle(input:String){
        _titulo.update {
            input
        }
    }

    fun changeFav(boolean: Boolean){
        _isFav.update { boolean }
    }
    fun changeIngredients(input:String){
        _ingredientes.update {
            input
        }
    }
    fun changeDesc(input:String){
        _descripcion.update {
            input
        }
    }
    fun changeInstruccions(input:String){
        _instrucciones.update {
            input
        }
    } fun changeTime(input:String){
        _tiempo.update {
            input.toInt()
        }
    }
    fun changePersonas(input:String){
        _personas.update {
            input.toInt()
        }
    }

    fun setImage(uri: Uri?){
        _image.update {
            uri
        }
    }
    fun setBitmap(bitmap: Bitmap?){
        _bitmap.update { bitmap }
    }

    fun clearFields() {
        _personas.update { 0 }
        _tiempo.update { 0 }
        _ingredientes.update { "" }
        _titulo.update { "" }
        _descripcion.update { "" }
        _instrucciones.update { "" }
        _image.update { null }
        _bitmap.update { null }
        _isFav.value = false

        sideTag.update { false }
        breakTag.update { false }
        dinnerTag.update { false }
        dessertTag.update { false }
        dishTag.update { false }
        mealTag.update { false }

    }

    fun getTagString():String{
        var tagString = ""
        tags.value.forEach {
            tagString = tagString.plus("$it, ")
        }
        return tagString
    }

    fun addRecipe() {
        viewModelScope.launch {
            var tagString = ""
            tags.value.forEach {
                tagString = tagString.plus("$it, ")
            }
            Log.w(TAG, "tag: $tagString")
            _recipe.update { Recipe(
                title = titulo.value,
                desc = descripcion.value,
                ingredientes = ingredientes.value,
                personas = personas.value,
                isFav = isFav.value,
                instrucciones = instrucciones.value,
                image = bitmap.value,
                tags = tagString,
                tiempo = tiempo.value
            ) }
            _recipe.value?.let {
                recipesRepository.addNewRecipe(
                    it
                )
            }
        }
    }

    fun clearRecipe(){
        _recipe.update {
            Recipe(
                title = "",
                desc = "",
                tiempo = 0,
                personas = 0,
                ingredientes = "",
                isFav = false,
                tags = "",
                instrucciones = ""

            )
        }
    }

    fun changeIsDialogShown(boolean: Boolean){
        _isDialogShown.update { boolean }
    }

    fun addTag(tag:String){
        Log.w(TAG, tag)
        _tags.value.add(tag)
    }

    fun removeTag(tag: String){
        _tags.value.remove(tag)
    }

    fun setValues(recipe: Recipe) {
        _personas.update { recipe.personas }
        _tiempo.update { recipe.tiempo }
        _ingredientes.update { recipe.ingredientes }
        _titulo.update {   recipe.title}
        _descripcion.update { recipe.desc }
        _instrucciones.update { recipe.instrucciones }
        _bitmap.update { recipe.image }
        _isFav.update { recipe.isFav }
        setTags(recipe.tags)
    }

    private fun setTags(tags: String) {

        if(tags.contains("Entrada")){
            sideTag.update { true }
        }
        if(tags.contains("Plato fuerte")){
            dishTag.update { true }
        }
        if(tags.contains("Postre")){
            dessertTag.update { true }
        }
        if(tags.contains("Desayuno")){
            breakTag.update { true }
        }
        if(tags.contains("Comida")){
            mealTag.update { true }
        }
        if(tags.contains("Cena")){
            dinnerTag.update { true }
        }
    }

    fun isEditMode() {
        _isEditMode.update {
            !it
        }
    }

}