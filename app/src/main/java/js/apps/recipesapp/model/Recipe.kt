package js.apps.recipesapp.model

import android.graphics.Bitmap
import android.net.Uri

data class Recipe(
    val id:Int? = null,
    val title:String,
    val desc:String,
    val ingredientes:String,
    val instrucciones:String,
    val tags:String,
    val tiempo:Int,
    val image: Bitmap? = null,
    val personas:Int,
    var isFav:Boolean
)
