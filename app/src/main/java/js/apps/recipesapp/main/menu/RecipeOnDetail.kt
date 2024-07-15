package js.apps.recipesapp.main.menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import js.apps.recipesapp.R
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.utils.Fonts.poppins
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.NewRecipeVM
import js.apps.recipesapp.viewModel.RecipeVM

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class
)
@Composable
fun RecipeOnDetailScreen(viewModel:RecipeVM, onBack:() -> Unit,
                         onEditRecipe:(Recipe) -> Unit,
                         newRecipeVM: NewRecipeVM) {
    val recipe: Recipe? by viewModel.recipe.collectAsState()
    val isFav: Boolean by viewModel.isFav.collectAsState()
    val isEditing: Boolean by newRecipeVM.isEditMode.collectAsState()
    val isDialogShown: Boolean by viewModel.isDialogShown.collectAsState()

    val context = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            newRecipeVM.setImage(it)
            newRecipeVM.setBitmap(bitmap = it?.let { it1 ->
                getBitmapFromUri(
                    context = context,
                    it1
                )
            })
        })
    val titulo: String by newRecipeVM.titulo.collectAsState()
    val desc: String by newRecipeVM.descripcion.collectAsState()
    val tiempo: Int by newRecipeVM.tiempo.collectAsState()
    val ingredientes: String by newRecipeVM.ingredientes.collectAsState()
    val instrucciones: String by newRecipeVM.instrucciones.collectAsState()
    val personas: Int by newRecipeVM.personas.collectAsState()
    val selectedFav: Boolean by newRecipeVM.isFav.collectAsState()
    val bitmap: Bitmap? by newRecipeVM.bitmap.collectAsState()
    val uri: Uri? by newRecipeVM.image.collectAsState()

    val selectedSideState by newRecipeVM.sideTag.collectAsState()
    var selectedDish by remember { mutableStateOf(false) }
    var selectedDessert by remember { mutableStateOf(false) }
    var selectedBreak by remember { mutableStateOf(false) }
    var selectedDinner by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf(false) }



    BackHandler(enabled = true) {
        onBack()
    }

    LazyColumn(
        modifier = Modifier
            .background(colorResource(id = R.color.white))
            .fillMaxSize()
            .blur(if(isDialogShown) 20.dp else 0.dp)
            //.padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        stickyHeader {
            OptionsBar(
                isFav,
                changeFab = { viewModel.changeFav(it) },
                onDelete = { viewModel.onClickDelete() },
                onBack = { onBack() },
                onEditRecipe = {
                    val changedRecipe = recipe
                    onEditRecipe(
                        if (bitmap != null) {
                            changedRecipe!!.copy(
                                title = titulo,
                                desc = desc,
                                tiempo = tiempo,
                                personas = personas,
                                instrucciones = instrucciones,
                                ingredientes = ingredientes,
                                isFav = isFav,
                                image = bitmap,
                                tags = newRecipeVM.getTagString()
                            )
                        } else {
                            changedRecipe!!.copy(
                                title = titulo,
                                desc = desc,
                                tiempo = tiempo,
                                personas = personas,
                                instrucciones = instrucciones,
                                ingredientes = ingredientes,
                                isFav = isFav,
                                tags = newRecipeVM.getTagString()
                            )
                        }
                    )

                },
                isEditing = isEditing,
                setEditMode = { newRecipeVM.isEditMode() },
                recipe = recipe!!
            )
        }
        item {
            if (isEditing) {


                if (recipe!!.image != null) {
                    Button(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Text(text = context.getString(R.string.change))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Button(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }, modifier = Modifier.offset(110.dp, 60.dp)) {
                        Text(text = context.getString(R.string.add_image))
                    }
                    AsyncImage(
                        model = if (uri != null) uri else recipe!!.image,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                with(newRecipeVM) {
                    TextField(
                        value = titulo,
                        onValueChange = { changeTitle(it) },
                        modifier = Modifier.padding(start = 30.dp, top = 20.dp),
                        placeholder = { Text(text = context.getString(R.string.title)) },
                        textStyle = Styles.newRecipeStyle
                    )
                    TextField(
                        value = desc,
                        onValueChange = { changeDesc(it) },
                        modifier = Modifier.padding(start = 30.dp, top = 20.dp),
                        placeholder = { Text(text = context.getString(R.string.recipe_description)) },
                        textStyle = Styles.newRecipeStyle
                    )
                    Text(
                        text = context.getString(R.string.select_dishtype),
                        Modifier.padding(start = 20.dp, top = 15.dp), fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        FilterChip(selected = selectedSideState, onClick = {
                            if (!selectedSideState) {
                                newRecipeVM.addTag("Entrada")
                            } else {
                                newRecipeVM.removeTag("Entrada")
                            }
                            sideTag.value = !sideTag.value
                        },
                            label = { Text(text = context.getString(R.string.side)) })
                        FilterChip(selected = dishTag.value, onClick = {
                            if (!dishTag.value) {
                                newRecipeVM.addTag("Plato fuerte")
                            } else {
                                newRecipeVM.removeTag("Plato fuerte")
                            }
                            dishTag.value = !dishTag.value
                        },
                            label = { Text(text = context.getString(R.string.main_dish)) })
                        FilterChip(selected = dessertTag.value, onClick = {
                            if (!dessertTag.value) {
                                newRecipeVM.addTag("Postre")
                            } else {
                                newRecipeVM.removeTag("Postre")
                            }
                            dessertTag.value = !dessertTag.value
                        },
                            label = { Text(text = context.getString(R.string.desserts)) })
                        FilterChip(selected = breakTag.value, onClick = {
                            if (!breakTag.value) {
                                newRecipeVM.addTag("Desayuno")
                            } else {
                                newRecipeVM.removeTag("Desayuno")
                            }
                            breakTag.value = !breakTag.value
                        },
                            label = { Text(text = context.getString(R.string.breakfast)) })
                        FilterChip(selected = mealTag.value, onClick = {
                            if (!mealTag.value) {
                                newRecipeVM.addTag("Comida")
                            } else {
                                newRecipeVM.removeTag("Comida")
                            }
                            mealTag.value = !mealTag.value
                        },
                            label = { Text(text = context.getString(R.string.meal)) })
                        FilterChip(selected = dinnerTag.value, onClick = {
                            if (!dinnerTag.value) {
                                newRecipeVM.addTag("Cena")
                            } else {
                                newRecipeVM.removeTag("Cena")
                            }
                            dinnerTag.value = !dinnerTag.value
                        },
                            label = { Text(text = context.getString(R.string.dinner)) })
                    }

                    Row {
                        TextField(
                            value = tiempo.toString(),
                            onValueChange = { if (it == "") changeTime("0") else changeTime(it) },
                            modifier = Modifier
                                .padding(start = 30.dp, top = 20.dp)
                                .width(100.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text(text = context.getString(R.string.preparation_timee)) },
                        )
                        Text(
                            text = context.getString(R.string.preparation_timee),
                            fontFamily = poppins,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 20.dp, top = 35.dp)
                        )
                    }

                    TextField(
                        value = ingredientes,
                        onValueChange = { changeIngredients(it) },
                        modifier = Modifier
                            .padding(start = 30.dp, top = 20.dp)
                            .height(120.dp),
                        placeholder = { Text(text = context.getString(R.string.recipe_ingredients)) },
                        textStyle = Styles.newRecipeStyle
                    )
                    TextField(
                        value = instrucciones,
                        onValueChange = { changeInstruccions(it) },
                        modifier = Modifier
                            .padding(start = 30.dp, top = 20.dp)
                            .height(120.dp),
                        placeholder = { Text(text = context.getString(R.string.recipe_steps)) },
                        textStyle = Styles.newRecipeStyle
                    )
                    Row {
                        TextField(
                            value = personas.toString(),
                            onValueChange = {
                                if (it == "") changePersonas("0") else changePersonas(
                                    it
                                )
                            },
                            modifier = Modifier
                                .padding(start = 30.dp, top = 20.dp)
                                .width(100.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = Styles.newRecipeStyle,
                            placeholder = { Text(text = context.getString(R.string.servings)) },
                        )
                        Text(
                            text = context.getString(R.string.servings),
                            fontFamily = poppins,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 20.dp, top = 35.dp)
                        )
                    }
                }

            } else {
                if (recipe!!.image != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {

                        Image(
                            recipe!!.image!!.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.FillBounds
                        )

                    }
                }

                Text(
                    text = recipe!!.title,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 30.dp, top = 20.dp, bottom = 10.dp),
                    fontFamily = poppins, fontWeight = FontWeight.Bold, fontSize = 22.sp
                )
                if (recipe!!.desc.isNotBlank()) {
                    Text(
                        text = recipe!!.desc,
                        modifier = Modifier.padding(start = 30.dp),
                        style = Styles.newRecipeStyle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }


                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(120.dp)
                        .padding(start = 20.dp)
                        .clip(RoundedCornerShape(12.dp))

                        .background(MaterialTheme.colorScheme.surfaceBright)
                ) {

                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (time, timeText, ppl, pplText) = createRefs()
                        val guideline = createGuidelineFromStart(0.5f)
                        Icon(painter = painterResource(id = R.drawable.time),
                            contentDescription = "",
                            modifier = Modifier
                                .constrainAs(time) {
                                    top.linkTo(parent.top, margin = 16.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(guideline)
                                }, tint = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = "${recipe!!.tiempo} min",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .constrainAs(timeText) {
                                    top.linkTo(time.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(guideline)
                                    bottom.linkTo(parent.bottom)
                                })

                        Icon(painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "", modifier = Modifier
                                .constrainAs(ppl) {
                                    top.linkTo(parent.top, margin = 16.dp)
                                    start.linkTo(guideline)
                                    end.linkTo(parent.end)
                                }, tint = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = recipe!!.personas.toString(),
                            fontFamily = poppins,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .constrainAs(pplText) {
                                    top.linkTo(ppl.bottom)
                                    end.linkTo(parent.end)
                                    start.linkTo(guideline)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                if (recipe!!.ingredientes.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .background(
                                MaterialTheme.colorScheme.surfaceBright
                            )
                    ) {
                        Text(
                            text = "${context.getString(R.string.recipe_ingredients)}: \n${recipe!!.ingredientes}",
                            modifier = Modifier
                                .padding(10.dp),
                            style = Styles.newRecipeStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (recipe!!.instrucciones.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .background(
                                MaterialTheme.colorScheme.surfaceBright
                            )
                    ) {
                        Text(
                            text = "${context.getString(R.string.recipe_steps)}: \n${recipe!!.instrucciones}",
                            modifier = Modifier
                                .padding(10.dp),
                            style = Styles.newRecipeStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                /*Spacer(modifier = androidx.compose.ui.Modifier.height(35.dp))
            Button(
                onClick = {
                    if (titulo != "") viewModel.addRecipe() else android.widget.Toast.makeText(
                        context,
                        "Añada un titulo",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }, shape = RoundedCornerShape(10.dp), colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = js.apps.recipesapp.R.color.teal)
                ), modifier = Modifier.padding(start = 50.dp)
            ) {
                Text(
                    text = "Agregar receta",
                    fontFamily = Fonts.poppins,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
            Spacer(modifier = androidx.compose.ui.Modifier.height(25.dp))
            Button(
                onClick = { viewModel.clearFields() },
                shape = RoundedCornerShape(10.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = js.apps.recipesapp.R.color.clear_red)
                ),
                modifier = Modifier.padding(start = 50.dp)
            ) {
                Text(text = "Limpiar", fontFamily = Fonts.poppins, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }*/

            }

            if (isDialogShown) {
                CustomDialogDelete(onDismiss = { viewModel.onClickDelete() }) {
                    viewModel.deleteRecipe(recipe!!)
                    viewModel.onClickDelete()
                    onBack()
                }
            }
        }
    }
}




@Composable
fun OptionsBar(
    isFav:Boolean,
    changeFab:(Boolean) -> Unit,
    onDelete:() -> Unit,
    onBack:() -> Unit,
    onEditRecipe: () -> Unit,
    setEditMode: () -> Unit,
    isEditing:Boolean,
    recipe: Recipe
) {
    val context = LocalContext.current
    Box(modifier = Modifier
        //.clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.secondary)
        .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {

            val (delete, returnbtn, share, edit, fav) = createRefs()
            Icon(painter = painterResource(id = R.drawable.return_btn), "", modifier = Modifier
                .constrainAs(returnbtn) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = 20.dp)
                }
                .clickable {
                    onBack()
                }, tint = MaterialTheme.colorScheme.onSecondary)
            if(isEditing){
                Image(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = "",
                    modifier = Modifier
                        .constrainAs(edit) {
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                            end.linkTo(share.start, margin = 20.dp)
                        }
                        .clickable {
                            onEditRecipe()
                        })
            }else {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "",
                    modifier = Modifier
                        .constrainAs(edit) {
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                            end.linkTo(share.start, margin = 20.dp)
                        }
                        .clickable {
                            setEditMode()
                        }, tint = MaterialTheme.colorScheme.onSecondary)
            }
            Image(painter = if (!isFav) painterResource(id = R.drawable.fav) else painterResource(
                id = R.drawable.selected_fav
            ), contentDescription = "", modifier = Modifier
                .constrainAs(fav) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    end.linkTo(edit.start, margin = 20.dp)
                }
                .clickable { changeFab(!isFav) })
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "",
                modifier = Modifier.constrainAs(share) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    end.linkTo(delete.start, margin = 20.dp)
                }.clickable {
                    compartirReceta(context, recipe)
                }, tint = MaterialTheme.colorScheme.onSecondary)

            Icon(painter = painterResource(id = R.drawable.delete),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(delete) {
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                    }
                    .clickable {
                        onDelete()
                    }, tint = MaterialTheme.colorScheme.onSecondary)


        }
    }
}

private fun compartirReceta(context: Context, recipe: Recipe) {
    val texto = "${recipe.title}\n\n${recipe.desc} \n" +
            "\nIngredientes:\n ${recipe.ingredientes} Instrucciones: \n" +
            recipe.instrucciones + "\n\nReceta compartida por RecipeApp"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, texto)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            bitmap = BitmapFactory.decodeStream(it)
            it.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}