package js.apps.recipesapp.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id:Int = 0,
    @ColumnInfo(name = "titulo") val title:String,
    @ColumnInfo(name = "descripcion")val desc:String,
    @ColumnInfo(name = "ingredientes")val ingredientes:String,
    @ColumnInfo(name = "instrucciones")val instrucciones:String,
    @ColumnInfo(name = "etiquetas")val tags:String,
    @ColumnInfo(name = "tiempo")val tiempo:Int,
    @ColumnInfo(name = "imagen")val image: Bitmap? = null,
    @ColumnInfo(name = "porcion")val personas:Int,
    @ColumnInfo(name = "isFav")val isFav:Boolean
)
