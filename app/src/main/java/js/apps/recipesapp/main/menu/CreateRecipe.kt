package js.apps.recipesapp.main.menu

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import coil.compose.AsyncImage
import js.apps.recipesapp.R
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.utils.Fonts.poppins
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.utils.Styles.newRecipeStyle
import js.apps.recipesapp.viewModel.NewRecipeVM
import js.apps.recipesapp.viewModel.RecipeVM
import js.apps.recipesapp.viewModel.SearchViewModel
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun CreateRecipeScreen(viewModel: NewRecipeVM, goToDetail:(Recipe) -> Unit, onBack:() -> Unit,
                       recipeVM: RecipeVM,
                       goToBuyPremium:() -> Unit) {

    var uris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val context = LocalContext.current
    var isSheetOpen by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val pref = PremiumSharedPref(context)
    val isUserPremium = pref.obtenerValorBooleano()
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            viewModel.setImage(it)
            viewModel.setBitmap(bitmap = it?.let { it1 ->
                getBitmapFromUri(
                    context = context,
                    it1
                )
            })
        })

    val titulo: String by viewModel.titulo.collectAsState()
    val desc: String by viewModel.descripcion.collectAsState()
    val tiempo: Int by viewModel.tiempo.collectAsState()
    val ingredientes: String by viewModel.ingredientes.collectAsState()
    val instrucciones: String by viewModel.instrucciones.collectAsState()
    val personas: Int by viewModel.personas.collectAsState()
    val selectedFav: Boolean by viewModel.isFav.collectAsState()
    val uri: Uri? by viewModel.image.collectAsState()

    var selectedSide by remember { mutableStateOf(false) }
    var selectedDish by remember { mutableStateOf(false) }
    var selectedDessert by remember { mutableStateOf(false) }
    var selectedBreak by remember { mutableStateOf(false) }
    var selectedDinner by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    ConstraintLayout( modifier = Modifier.fillMaxSize()) {
        val (ad, content) = createRefs()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, contentPadding = if (isUserPremium) PaddingValues(bottom = 0.dp) else PaddingValues(bottom = 80.dp)
    ) {

        stickyHeader {
            TopBar {
                onBack()
            }
        }

        item {
            if (uri != null) {
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ImageOnly)
                    )
                }) {
                    Text(text = context.getString(R.string.change))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(start = 12.dp, end = 12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Button(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ImageOnly)
                    )
                }, modifier = Modifier.offset(110.dp, 60.dp)) {
                    Text(text = context.getString(R.string.add_image))
                }
                AsyncImage(
                    model = uri, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            with(viewModel) {
                TextField(
                    value = titulo,
                    onValueChange = { changeTitle(it) },
                    modifier = Modifier.padding(start = 30.dp, top = 20.dp),
                    placeholder = { Text(text = context.getString(R.string.title)) },
                    textStyle = newRecipeStyle
                )
                TextField(
                    value = desc,
                    onValueChange = { changeDesc(it) },
                    modifier = Modifier.padding(start = 30.dp, top = 20.dp),
                    placeholder = { Text(text = context.getString(R.string.recipe_description)) },
                    textStyle = newRecipeStyle
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
                    FilterChip(selected = selectedSide, onClick = {
                        if (!selectedSide) {
                            viewModel.addTag("Entrada")
                        } else {
                            viewModel.removeTag("Entrada")
                        }
                        selectedSide = !selectedSide
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.side),
                                style = Styles.filterChipStyle
                            )
                        })
                    FilterChip(selected = selectedDish, onClick = {
                        if (!selectedDish) {
                            viewModel.addTag("Plato fuerte")
                        } else {
                            viewModel.removeTag("Plato fuerte")
                        }
                        selectedDish = !selectedDish
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.main_dish),
                                style = Styles.filterChipStyle
                            )
                        })
                    FilterChip(selected = selectedDessert, onClick = {
                        if (!selectedDessert) {
                            viewModel.addTag("Postre")
                        } else {
                            viewModel.removeTag("Postre")
                        }
                        selectedDessert = !selectedDessert
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.desserts),
                                style = Styles.filterChipStyle
                            )
                        })
                    FilterChip(selected = selectedBreak, onClick = {
                        if (!selectedBreak) {
                            viewModel.addTag("Desayuno")
                        } else {
                            viewModel.removeTag("Desayuno")
                        }
                        selectedBreak = !selectedBreak
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.breakfast),
                                style = Styles.filterChipStyle
                            )
                        })
                    FilterChip(selected = selectedMeal, onClick = {
                        if (!selectedMeal) {
                            viewModel.addTag("Comida")
                        } else {
                            viewModel.removeTag("Comida")
                        }
                        selectedMeal = !selectedMeal
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.meal),
                                style = Styles.filterChipStyle
                            )
                        })
                    FilterChip(selected = selectedDinner, onClick = {
                        if (!selectedDinner) {
                            viewModel.addTag("Cena")
                        } else {
                            viewModel.removeTag("Cena")
                        }
                        selectedDinner = !selectedDinner
                    },
                        label = {
                            Text(
                                text = context.getString(R.string.dinner),
                                style = Styles.filterChipStyle
                            )
                        })
                }
                Row {
                    Text(
                        text = context.getString(R.string.add_favorite),
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 20.dp, top = 26.dp, end = 8.dp)
                    )
                    Icon(painter = if (!selectedFav) painterResource(id = R.drawable.fav) else painterResource(
                        id = R.drawable.selected_fav
                    ),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 26.dp)
                            .clickable { viewModel.changeFav(!selectedFav) })

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
                        text = context.getString(R.string.preparation_time),
                        fontFamily = poppins,
                        fontWeight = FontWeight.Light,
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
                    textStyle = newRecipeStyle
                )
                TextField(
                    value = instrucciones,
                    onValueChange = { changeInstruccions(it) },
                    modifier = Modifier
                        .padding(start = 30.dp, top = 20.dp)
                        .height(120.dp),
                    placeholder = { Text(text = context.getString(R.string.recipe_steps)) },
                    textStyle = newRecipeStyle
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 30.dp)) {
                    Spacer(modifier = Modifier.width(15.dp))
                    if (personas != 0){
                        Icon(painter = painterResource(id = R.drawable.minus),
                            contentDescription = "", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.clickable {
                                changePersonas((personas - 1).toString())
                            })
                    }

                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = personas.toString(), style = Styles.filterChipStyle, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(15.dp))
                    Icon(painter = painterResource(id = R.drawable.plus), contentDescription = "",
                        modifier = Modifier.clickable {
                            changePersonas((personas + 1).toString())
                        }, tint = MaterialTheme.colorScheme.onSurfaceVariant)

                    Text(
                        text = context.getString(R.string.servings),
                        fontFamily = poppins,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(35.dp))
                Button(
                    onClick = {

                        if (isUserPremium) {
                            if (titulo != "") {
                                viewModel.addRecipe()
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.added),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Thread.sleep(2000)
                                viewModel.recipe.value?.let { goToDetail(it) }
                            } else Toast.makeText(
                                context,
                                context.getString(R.string.add_title),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (recipeVM.totalList.value.size <= 20) {
                                if (titulo != "") {
                                    viewModel.addRecipe()
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.added),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Thread.sleep(2000)
                                    viewModel.recipe.value?.let { goToDetail(it) }
                                } else Toast.makeText(
                                    context,
                                    context.getString(R.string.add_title),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                            }
                        }

                    }, shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(start = 50.dp)
                ) {
                    Text(
                        text = context.getString(R.string.add_recipe),
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = { viewModel.clearFields() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.clear_red)
                    ),
                    modifier = Modifier.padding(start = 50.dp)
                ) {
                    Text(
                        text = context.getString(R.string.clean),
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.limpiar),
                        contentDescription = "",

                        )
                }
                Spacer(modifier = Modifier.height(15.dp))

            }

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { isSheetOpen = false }) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Has llegado al límmite de recetas. Hazte " +
                                    "premium para agregar más recetas, quitar los anuncios y descargar recetas en línea por sólo $80",
                            style = Styles.recipeInstructionTitleStyle
                        )
                        Button(onClick = { goToBuyPremium() }) {
                            Text(text = "Comprar premium")
                            Spacer(modifier = Modifier.height(15.dp))
                            Image(
                                painter = painterResource(id = R.drawable.baseline_shopping_cart_24),
                                contentDescription = ""
                            )
                        }
                    }

                }

            }


        }
    }
        BannerAd(adUnitId = "ca-app-pub-8489206541399786/1060387806", modifier = Modifier
            .constrainAs(ad) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                visibility = if (isUserPremium) {
                    Visibility.Gone
                } else {
                    Visibility.Visible
                }
            }
            .fillMaxWidth())

    }

}





private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            bitmap = comprimirBitmapDesdeInputStream(it) // Ajusta la calidad (0-100) según tus necesidades
            it.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}



@Composable
fun TopBar(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .height(90.dp)

            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            ), horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.return_btn),
            contentDescription = "", modifier = Modifier
                .padding(start = 25.dp)
                .clickable {
                    onBack()
                },
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            context.getString(R.string.new_recipe_title),
            modifier = Modifier.padding(start = 35.dp),
            fontFamily = poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

    }
}

fun comprimirBitmapDesdeInputStream(inputStream: InputStream): Bitmap? {
    // 1. Decodifica la imagen original
    val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null

    // 2. Escala la imagen a 250x250
    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 250, 250, true)

    // 3. Comprime la imagen en memoria
    val outputStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Ajusta la calidad según tus necesidades

    // 4. Crea un nuevo Bitmap a partir de los datos comprimidos
    val compressedByteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
}

