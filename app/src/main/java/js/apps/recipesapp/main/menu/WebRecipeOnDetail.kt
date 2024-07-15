package js.apps.recipesapp.main.menu

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import js.apps.recipesapp.R
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.ApiResults
import js.apps.recipesapp.utils.Fonts
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.RecipeVM
import js.apps.recipesapp.viewModel.SearchViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun WebRecipeOnDetail(
    searchViewModel: SearchViewModel,
    onBack: () -> Unit,

    //sharedTransitionScope: SharedTransitionScope,
    //animatedVisibilityScope: AnimatedVisibilityScope
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var isOnTop by remember { mutableStateOf(false) }
    val recipe by searchViewModel.recipe.collectAsState()
    val searchState by searchViewModel.searchState.collectAsState()
    var savedRecipe by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isUserPremium by searchViewModel.isUserPremium.collectAsState()

    BackHandler(true) {
        Toast.makeText(context, "on back", Toast.LENGTH_SHORT).show()
        onBack()
    }
    when (searchState) {
        ApiResults.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Text(
                    text = "Loading recipe...",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        is ApiResults.Error -> {
            Toast.makeText(
                context,
                (searchState as ApiResults.Error).exception.message,
                Toast.LENGTH_SHORT
            ).show()
            searchViewModel.resetSearchState()
        }

        is ApiResults.Success -> {
            searchViewModel.setRecipe((searchState as ApiResults.Success<Result>).data)
            Log.w("TAG", "Success")

            searchViewModel.resetSearchState()
        }

        is ApiResults.NoSuccess -> {
            Log.w("TAG", "No succes")
            searchViewModel.setRecipes(emptyList())
            Toast.makeText(
                context,
                (searchState as ApiResults.NoSuccess<List<Result>>).message,
                Toast.LENGTH_SHORT
            ).show()
            searchViewModel.resetSearchState()
        }

        ApiResults.Finished -> {
            if (recipe != null) {
                savedRecipe = searchViewModel.containsElement(recipe!!.id)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    WindowInsets.statusBars
                                        .asPaddingValues()
                                        .calculateTopPadding()
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black,
                                            Color.Transparent
                                        ),

                                        )
                                )
                        )
                    }
                    item {

                        AsyncImage(
                            model = recipe!!.image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            /*.sharedElement(
                        rememberSharedContentState(key = "image-${recipe?.id}"),
                        animatedVisibilityScope
                    )*/
                            contentScale = ContentScale.Fit,
                        )
                    }
                    stickyHeader {


                        Row(
                            modifier = Modifier


                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomStart = 12.dp,
                                        bottomEnd = 12.dp
                                    )
                                )
                                .background(
                                    MaterialTheme.colorScheme.primary
                                )
                                .onGloballyPositioned {
                                    isOnTop = it.positionInParent().y == 0.0F

                                }
                                .padding(
                                    if (isOnTop) {
                                        WindowInsets.statusBars.asPaddingValues()
                                    } else {
                                        PaddingValues(0.dp)
                                    }
                                )


                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 8.dp)
                                    .clickable {
                                        onBack()
                                    }
                                    .size(40.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = recipe!!.title,
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 8.dp, bottom = 8.dp)
                                    .wrapContentHeight()
                                    .widthIn(max = 300.dp),
                                style = Styles.recipeTitleStyle,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 10.dp, end = 8.dp, bottom = 9.dp)
                                    .clickable { isSheetOpen = true }
                                    .size(40.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }


                    item {
                        RecipeContent(recipe!!)
                        if (isSheetOpen) {


                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = { isSheetOpen = false }
                            ) {
                                OptionsModal(text = "Share", res = R.drawable.baseline_share_24) {
                                    compartirReceta(context, recipe!!)
                                    isSheetOpen = false
                                }
                                OptionsModal(
                                    text = "Mark as favourite",
                                    res = R.drawable.baseline_bookmark_24
                                ) {
                                    if (savedRecipe) {
                                        searchViewModel.deleteRecipe()
                                        savedRecipe = false
                                    } else {
                                        if (!searchViewModel.isUserPremium.value) {
                                            if (searchViewModel.savedRecipes.value.size <= 10) {
                                                searchViewModel.saveRecipe()
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Receta agregada a favoritos",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                savedRecipe = true
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "No puedes guardar mas de 15 recetas sin premium",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            searchViewModel.saveRecipe()
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Receta agregada a favoritos",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            savedRecipe = true
                                        }


                                    }
                                }
                                OptionsModal(
                                    text = "Download",
                                    res = R.drawable.baseline_save_alt_24
                                ) {
                                    if (isUserPremium) {
                                        searchViewModel.downloadRecipe(recipe!!)
                                        Toast.makeText(
                                            context,
                                            "Receta descargada",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Buy premium to use this feature",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No recipe found", style = Styles.recipeInstructionTitleStyle)
                }
            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun WebRecipeOnDetailDownload(
    recipeVM: RecipeVM,
    onBack: () -> Unit,

    //sharedTransitionScope: SharedTransitionScope,
    //animatedVisibilityScope: AnimatedVisibilityScope
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var isOnTop by remember { mutableStateOf(false) }
    val recipe by recipeVM.downloadRecipe.collectAsState()
    val context = LocalContext.current



    BackHandler(true) {
        Toast.makeText(context, "on back", Toast.LENGTH_SHORT).show()
        onBack()
    }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            WindowInsets.statusBars
                                .asPaddingValues()
                                .calculateTopPadding()
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Transparent
                                ),

                                )
                        ))
            }
            item {

                AsyncImage(
                    model = recipe!!.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    /*.sharedElement(
                        rememberSharedContentState(key = "image-${recipe?.id}"),
                        animatedVisibilityScope
                    )*/
                    contentScale = ContentScale.Fit,
                )
            }
            stickyHeader {


                Row(
                    modifier = Modifier


                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                        .onGloballyPositioned {
                            isOnTop = it.positionInParent().y == 0.0F

                        }
                        .padding(
                            if (isOnTop) {
                                WindowInsets.statusBars.asPaddingValues()
                            } else {
                                PaddingValues(0.dp)
                            }
                        )


                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 8.dp)
                            .clickable {
                                onBack()
                            },
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = recipe!!.title,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 8.dp, bottom = 8.dp)
                            .wrapContentHeight()
                            .widthIn(max = 300.dp),
                        style = Styles.recipeTitleStyle,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter =painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 8.dp, bottom = 9.dp)
                            .clickable { isSheetOpen = true },
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }


            item {
                RecipeContent(recipe!!)
                if(isSheetOpen) {


                    ModalBottomSheet(sheetState = sheetState, onDismissRequest = { isSheetOpen = false }
                    ) {
                        OptionsModal(text = "Share", res = R.drawable.baseline_share_24){
                            compartirReceta(context, recipe!!)
                            isSheetOpen = false
                        }
                        OptionsModal(text = "Remove download", res = R.drawable.download_off){
                            Toast.makeText(context, "Download removed", Toast.LENGTH_SHORT).show()
                            recipeVM.deleteDownload(recipe!!)
                            onBack()
                        }
                    }
                }
            }
        }
    }



@Composable
fun OptionsModal(text: String, res: Int, onClic: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClic()
            }
            .padding(end = 10.dp, start = 16.dp)
            .height(80.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.padding(start = 10.dp), style = Styles.modalItems, color = MaterialTheme.colorScheme.inverseSurface, fontSize = 24.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = painterResource(id = res), contentDescription = null, modifier = Modifier.padding(10.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
fun RecipeContent(recipe: Result) {
    val context = LocalContext.current
    var ingredientString = ""
    recipe.extendedIngredients.forEach {
        ingredientString = ingredientString.plus("${it.original} \n")
    }
    var instructionString = ""
    recipe.analyzedInstructions[0].steps.forEach {
        instructionString = instructionString.plus("${it.number}: ${it.step} \n")
    }
    val tagsString = recipe.dishTypes.toMutableList()
    if (recipe.vegan) {
        tagsString.add("Vegan")
    }
    if (recipe.vegetarian) {
        tagsString.add("Vegetarian")
    }
    if (recipe.glutenFree) {
        tagsString.add("Gluten Free")
    }
    if (recipe.dairyFree) {
        tagsString.add("Dairy Free")
    }
    if (recipe.cheap) {
        tagsString.add("Cheap")
    }




    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (ingredients, instructions, source, dishType, mealType, calories, tags, titleInfo) = createRefs()
        Text(text = "Recipe tags", modifier = Modifier.constrainAs(titleInfo) {
            top.linkTo(parent.top, 10.dp)
            start.linkTo(parent.start, margin = 6.dp)
        }, style = Styles.recipeInstructionTitleStyle, color = MaterialTheme.colorScheme.inverseSurface)

        LazyRow(modifier = Modifier.constrainAs(dishType) {
            top.linkTo(titleInfo.bottom, 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, contentPadding = PaddingValues(start = 6.dp, end = 6.dp)) {
            items(tagsString) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(12.dp),
                            style = Styles.newRecipeStyle
                        )
                    }
                }
            }

        }
        TimeLayout(
            modifier = Modifier
                .constrainAs(mealType) {
                    top.linkTo(dishType.bottom, 10.dp)
                    start.linkTo(parent.start, margin = 6.dp)
                    end.linkTo(tags.start, margin = 6.dp)
                    width = Dimension.fillToConstraints
                }, readyInTime = recipe.readyInMinutes.toString()
        )

        ServingsLayout(
            servingNumber = recipe.servings.toString(),
            modifier = Modifier.constrainAs(tags) {
                top.linkTo(dishType.bottom, 10.dp)
                start.linkTo(mealType.end, margin = 6.dp)
                end.linkTo(parent.end, margin = 6.dp)
                width = Dimension.fillToConstraints
            })

        Column(
            modifier = Modifier.constrainAs(ingredients) {
                top.linkTo(tags.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                height = Dimension.fillToConstraints
                width = Dimension.wrapContent

            }) {
            Text(
                text = context.getString(R.string.recipe_ingredients) ,
                Modifier.padding(bottom = 8.dp, start = 16.dp),
                style = Styles.recipeInstructionTitleStyle,
                color = MaterialTheme.colorScheme.inverseSurface

            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = ingredientString, modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .wrapContentHeight()
                        .padding(6.dp), style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Column(
            modifier = Modifier.constrainAs(instructions) {
                top.linkTo(ingredients.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                height = Dimension.fillToConstraints
                width = Dimension.wrapContent

            }) {
            Text(
                text = context.getString(R.string.recipe_steps),
                Modifier.padding(bottom = 8.dp, start = 16.dp),
                style = Styles.recipeInstructionTitleStyle,
                color = MaterialTheme.colorScheme.inverseSurface

            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = instructionString, modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .wrapContentHeight()
                        .padding(6.dp), style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Text("Source: ${recipe.sourceName}", modifier = Modifier.constrainAs(calories) {
            top.linkTo(instructions.bottom, margin = 10.dp)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }, style = Styles.recipeInstructionStyle)

        Text("Porciones: ${recipe.servings}", modifier = Modifier.constrainAs(source) {
            top.linkTo(calories.bottom, margin = 10.dp)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }, style = Styles.recipeInstructionStyle)
    }
}

@Composable
fun RecipeContent(recipe: ResultEntity) {

    val context = LocalContext.current
    val tagsString = mutableListOf<String>()
    if(recipe.veryHealthy){
        tagsString.add("Very Healthy")
    }
    if (recipe.vegan) {
        tagsString.add("Vegan")
    }
    if (recipe.vegetarian) {
        tagsString.add("Vegetarian")
    }
    if (recipe.glutenFree) {
        tagsString.add("Gluten Free")
    }
    if (recipe.dairyFree) {
        tagsString.add("Dairy Free")
    }
    if (recipe.cheap) {
        tagsString.add("Cheap")
    }




    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {
        val (ingredients, instructions, source, dishType, mealType, calories, tags, titleInfo) = createRefs()
        Text(text = "Recipe tags", modifier = Modifier.constrainAs(titleInfo) {
            top.linkTo(parent.top, 10.dp)
            start.linkTo(parent.start, margin = 6.dp)
        }, style = Styles.recipeInstructionTitleStyle, color = MaterialTheme.colorScheme.inverseSurface)

        LazyRow(modifier = Modifier.constrainAs(dishType) {
            top.linkTo(titleInfo.bottom, 10.dp)
            start.linkTo(parent.start, margin = 6.dp)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }) {
            items(tagsString) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(12.dp),
                            style = Styles.newRecipeStyle,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

        }
        TimeLayout(
            modifier = Modifier
                .constrainAs(mealType) {
                    top.linkTo(dishType.bottom, 10.dp)
                    start.linkTo(parent.start, margin = 6.dp)
                    end.linkTo(tags.start, margin = 6.dp)
                    width = Dimension.fillToConstraints
                }, readyInTime = recipe.readyInMinutes.toString()
        )

        ServingsLayout(
            servingNumber = recipe.servings.toString(),
            modifier = Modifier.constrainAs(tags) {
                top.linkTo(dishType.bottom, 10.dp)
                start.linkTo(mealType.end, margin = 6.dp)
                end.linkTo(parent.end, margin = 6.dp)
                width = Dimension.fillToConstraints
            })

        Column(
            modifier = Modifier.constrainAs(ingredients) {
                top.linkTo(tags.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                height = Dimension.fillToConstraints
                width = Dimension.wrapContent

            }) {

            Text(
                text = context.getString(R.string.recipe_ingredients),
                Modifier.padding(bottom = 8.dp, start = 16.dp),
                style = Styles.recipeInstructionTitleStyle,
                color = MaterialTheme.colorScheme.inverseSurface

            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 10.dp, top = 10.dp)

            ) {
                Text(
                    text = recipe.extendedIngredients, modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .wrapContentHeight()
                        .padding(6.dp), style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Column(
            modifier = Modifier.constrainAs(instructions) {
                top.linkTo(ingredients.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                height = Dimension.fillToConstraints
                width = Dimension.wrapContent

            }) {
            Text(
                text = context.getString(R.string.recipe_steps),
                Modifier.padding(bottom = 8.dp, start = 16.dp),
                style = Styles.recipeInstructionTitleStyle,
                color = MaterialTheme.colorScheme.inverseSurface

            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = recipe.analyzedInstructions, modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .wrapContentHeight()
                        .padding(6.dp), style = Styles.recipeInstructionStyle,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Text("Source: ${recipe.sourceName}", modifier = Modifier.constrainAs(calories) {
            top.linkTo(instructions.bottom, margin = 10.dp)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }, style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onPrimaryContainer)

        Text("Porciones: ${recipe.servings}", modifier = Modifier.constrainAs(source) {
            top.linkTo(calories.bottom, margin = 10.dp)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }, style = Styles.recipeInstructionStyle,
            color = MaterialTheme.colorScheme.inverseSurface)
    }
}

@Composable
fun TimeLayout(readyInTime: String, modifier: Modifier) {
    Card(onClick = {  }, modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.caramel),
                            colorResource(id = R.color.koko)
                        )
                    )
                )
        ) {
            val (time, timeText, icon) = createRefs()

            Image(painter = painterResource(id = R.drawable.luch_time), contentDescription = null,
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    })
            Text(text = readyInTime, modifier = Modifier.constrainAs(time) {
                top.linkTo(icon.top, margin = 16.dp)
                start.linkTo(icon.end, margin = 8.dp)

            }, style = Styles.recipeInstructionTitleStyle)
            Text(text = "minutes", modifier = Modifier.constrainAs(timeText) {
                top.linkTo(time.bottom)
                start.linkTo(icon.end, margin = 8.dp)
            }, style = Styles.recipeInstructionStyle)
        }
    }

}

@Composable
fun ServingsLayout(servingNumber: String, modifier: Modifier) {
    Card(onClick = {  }, modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.clear_green),
                            colorResource(id = R.color.blue_clear)
                        )
                    )
                )
        ) {
            val (servings, icon, portionts) = createRefs()

            Image(painter = painterResource(id = R.drawable.servings), contentDescription = null,
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .fillMaxHeight())
            Text(text = servingNumber, modifier = Modifier.constrainAs(servings) {
                top.linkTo(icon.top, margin = 16.dp)
                start.linkTo(icon.end, margin = 8.dp)
            }, style = Styles.recipeInstructionTitleStyle)
            Text(text = "servings", modifier = Modifier.constrainAs(portionts) {
                top.linkTo(servings.bottom)
                start.linkTo(icon.end, margin = 8.dp)
            }, style = Styles.recipeInstructionStyle)
        }
    }

}

private fun compartirReceta(context: Context, recipe: Result) {
    val texto = " Check this spectacular recipe: ${recipe.title}\n\n Source: ${recipe.sourceName} \n" +
            "\nLink:\n ${recipe.sourceUrl}"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, texto)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

private fun compartirReceta(context: Context, recipe: ResultEntity) {
    val texto = " Check this spectacular recipe that i checked on My Recipebook: ${recipe.title}\n\n Source: ${recipe.sourceName} \n" +
            "\nLink:\n ${recipe.sourceUrl}"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, texto)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}


