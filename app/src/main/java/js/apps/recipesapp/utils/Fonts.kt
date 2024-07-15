package js.apps.recipesapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.sp
import js.apps.recipesapp.R
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.RecipeEntity
import js.apps.recipesapp.model.SavedEntity
import js.apps.recipesapp.model.spoonacular.Poster
import js.apps.recipesapp.utils.Fonts.poppins

object Fonts {
    val poppins = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
    )
}

object Styles{
    val modalItems =TextStyle(
        fontSize = 20.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.SemiBold
    )
    val newRecipeStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Normal
    )
    val searchRecipeStyle = TextStyle(
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Normal
    )

    val filterChipStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Light
    )

    val recipeTitleStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Bold
    )
    val recipeInstructionStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    val recipeInstructionTitleStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
}

fun RecipeEntity.toRecipe():Recipe{
    return Recipe(
        id = id,
        title = title,
        desc = desc,
        tags = tags,
        isFav = isFav,
        instrucciones = instrucciones,
        image = image,
        ingredientes = ingredientes,
        personas = personas,
        tiempo = tiempo
    )
}
fun Recipe.toEntity():RecipeEntity{
    return RecipeEntity(
        id = id!!,
        title = title,
        desc = desc,
        tags = tags,
        isFav = isFav,
        instrucciones = instrucciones,
        image = image,
        ingredientes = ingredientes,
        personas = personas,
        tiempo = tiempo
    )
}

fun SavedEntity.toPoster():Poster{
    return Poster(
        id = href,
        title = name,
        image = image,
        roomId = id)
}

fun Poster.toEntity():SavedEntity {
    return SavedEntity(
        href = id,
        name = title,
        image = image
    )
}

fun Rect.toIntRect(): IntRect {
    return IntRect(
        left.toInt(),
        top.toInt(),
        right.toInt(),
        bottom.toInt()
    )
}

fun Context.getActivityOrNull(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    return null
}