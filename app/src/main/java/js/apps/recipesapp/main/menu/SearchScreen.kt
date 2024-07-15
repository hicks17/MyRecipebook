package js.apps.recipesapp.main.menu

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import js.apps.recipesapp.R
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.ApiResults
import js.apps.recipesapp.utils.Fonts
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.SearchViewModel

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onClicRecipe: (Result) -> Unit,
    onClicSaved: () -> Unit,
    onDownload: (Result) -> Unit,
    goToBuyPremium: () -> Unit
    //sharedTransitionScope: SharedTransitionScope,
    //animatedVisibilityScope: AnimatedVisibilityScope

) {

    val active by searchViewModel.active.collectAsState()
    val query by searchViewModel.query.collectAsState()
    val suggestions by searchViewModel.suggestions.collectAsState()
    val recipes by searchViewModel.recipes.collectAsState()
    val type by searchViewModel.type.collectAsState()
    val searchState by searchViewModel.loginState.collectAsState()

    val selectedSideState by searchViewModel.sideTag.collectAsState()
    val selectedDishState by searchViewModel.dishTag.collectAsState()
    val selectedDessertState by searchViewModel.dessertTag.collectAsState()
    val selectedBreakState by searchViewModel.breakTag.collectAsState()
    val selectedMealState by searchViewModel.mealTag.collectAsState()
    val selectedDinnerState by searchViewModel.dinnerTag.collectAsState()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.statusBars.asPaddingValues())) {

/*

        Text(
            "Buscador de recetas en línea",
            fontFamily = Fonts.poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.padding(start = 16.dp, top = 6.dp, end = 6.dp).wrapContentSize()
        )*/
        SearchBar(

            modifier = Modifier
                .fillMaxWidth(),
            query = query,
            colors = SearchBarDefaults.colors(
                containerColor = Color.White
            ),
            onQueryChange = { searchViewModel.setQuery(it) },
            onSearch = {
                if (query.isBlank() && type.isBlank()) {

                    Toast.makeText(
                        context,
                        context.getString(R.string.search_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    searchViewModel.setActive()
                    searchViewModel.searchRecipes()
                    searchViewModel.setNewSearch()
                }
            },
            active = active,
            onActiveChange = { searchViewModel.setActive() },
            leadingIcon = {
                if (active) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                searchViewModel.setActive()
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.lens),
                        contentDescription = "", modifier = Modifier.padding(10.dp)
                    )
                }
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.close_64),
                        contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                searchViewModel.setQuery("")
                            }
                    )
                } else {
                    if (active) {
                        Image(
                            painter = painterResource(id = R.drawable.lens),
                            contentDescription = "", modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    if (type.isBlank()) {

                                        Toast
                                            .makeText(
                                                context,
                                                "Ingresa texto o tipo para buscar una receta",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    } else {
                                        searchViewModel.setActive()
                                        searchViewModel.searchRecipes()
                                        searchViewModel.setNewSearch()
                                    }
                                }
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = context.getString(R.string.search_online),
                    style = Styles.filterChipStyle,
                    fontSize = 18.sp
                )
            }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFFFFFFF))

            ) {
                // ...
                item {
                    Text(
                        context.getString(R.string.recent_searchs),
                        modifier = Modifier.padding(10.dp),
                        style = Styles.searchRecipeStyle,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
                items(suggestions) {

                    SearchRow(it, onRememberText = {
                        searchViewModel.setQuery(it)
                    }, onClicSearch = {
                        searchViewModel.setQuery(it)
                        searchViewModel.setActive()
                        searchViewModel.searchRecipes()

                    })
                }
                item {
                    if (suggestions.isNotEmpty()) {
                        Text(
                            context.getString(R.string.delete_history),
                            modifier = Modifier
                                .padding(14.dp)
                                .clickable { searchViewModel.deleteAllSearchs() }
                                .fillMaxWidth(),
                            color = Color(
                                0xFF142EBE
                            ),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontFamily = Fonts.poppins,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                item {
                    Text(
                        context.getString(R.string.search_by_tyoe),
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontFamily = Fonts.poppins,
                        fontWeight = FontWeight.Normal
                    )
                    with(searchViewModel) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            FilterChip(selected = selectedSideState, onClick = {
                                if (!selectedSideState) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("appetizer")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                sideTag.value = !sideTag.value
                            },
                                label = { Text(text = "Entrada", style = Styles.filterChipStyle) })
                            FilterChip(selected = selectedDishState, onClick = {
                                if (!dishTag.value) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("main course")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                dishTag.value = !dishTag.value
                            },
                                label = {
                                    Text(
                                        text = "Plato fuerte",
                                        style = Styles.filterChipStyle
                                    )
                                })
                            FilterChip(selected = selectedDessertState, onClick = {
                                if (!dessertTag.value) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("dessert")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                dessertTag.value = !dessertTag.value
                            },
                                label = { Text(text = "Postre", style = Styles.filterChipStyle) })
                            FilterChip(selected = selectedBreakState, onClick = {
                                if (!breakTag.value) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("breakfast")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                breakTag.value = !breakTag.value
                            },
                                label = { Text(text = "Desayuno", style = Styles.filterChipStyle) })
                            FilterChip(selected = selectedMealState, onClick = {
                                if (!mealTag.value) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("snack")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                mealTag.value = !mealTag.value
                            },
                                label = { Text(text = "Snack", style = Styles.filterChipStyle) })
                            FilterChip(selected = selectedDinnerState, onClick = {
                                if (!dinnerTag.value) {
                                    searchViewModel.clearTags()
                                    searchViewModel.addTag("salad")
                                } else {
                                    searchViewModel.removeTag()
                                }
                                dinnerTag.value = !dinnerTag.value
                            },
                                label = { Text(text = "Ensalada", style = Styles.filterChipStyle) })
                        }
                    }
                    Text(
                        context.getString(R.string.number_servings),
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        fontFamily = Fonts.poppins,
                        fontWeight = FontWeight.Normal
                    )
                    RangeSliderExample(searchViewModel)
                    Text(
                        context.getString(R.string.time_minutes),
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        fontFamily = Fonts.poppins,
                        fontWeight = FontWeight.Normal
                    )
                    SliderMinimalExample(searchViewModel)

                }

            }
        }
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable { onClicSaved() }
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {

                Text(
                    text = context.getString(R.string.bookmarks),
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseSurface, modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.baseline_bookmark_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 10.dp, top = 4.dp, bottom = 4.dp)
                )
            }
        }
        if (query.isNotBlank()) {
            Text(
                text = context.getString(R.string.results) + " \"$query\" ",
                fontFamily = Fonts.poppins,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.inverseSurface,
                fontSize = 22.sp,
                modifier = Modifier.padding(6.dp)
            )
        } else {
            if (type.isNotBlank()) {
                Text(
                    text = context.getString(R.string.results) + " \"$type\" ",
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
        EventsListener(searchViewModel, recipes, onDownload = { recipe ->
            if(searchViewModel.isUserPremium.value){
                searchViewModel.downloadRecipe(recipe)
                onDownload(recipe)
                Toast.makeText(context, context.getString(R.string.recipe_downloaded), Toast.LENGTH_SHORT).show()
            }else{
                isSheetOpen = true
            }

        } ){
            onClicRecipe(it)
        }
        if (isSheetOpen) {
            ModalBottomSheet(sheetState = sheetState, onDismissRequest = { isSheetOpen = false }) {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = context.getString(R.string.premium_offer),
                        style = Styles.recipeInstructionTitleStyle,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { goToBuyPremium() }) {
                        Text(text = context.getString(R.string.buy_premium))
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(painter = painterResource(id = R.drawable.baseline_shopping_cart_24), contentDescription = "")


                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }

    }


}

@Composable
fun EventsListener(
    searchViewModel: SearchViewModel,
    recipes: List<Result>,
    onDownload: (Result) -> Unit,
    onClicRecipe: (Result) -> Unit,

) {

    val searchState by searchViewModel.loginState.collectAsState()
    val context = LocalContext.current

    when (searchState) {
        is ApiResults.Error -> {
            searchViewModel.setRecipes(emptyList())
            Toast.makeText(
                context,
                (searchState as ApiResults.Error).exception.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        ApiResults.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = context.getString(R.string.loading),
                    modifier = Modifier.padding(top = 16.dp),
                    style = Styles.searchRecipeStyle,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }

        is ApiResults.NoSuccess -> {
            Log.w("TAG", "No succes")
            searchViewModel.setRecipes(emptyList())
            Toast.makeText(
                context,
                (searchState as ApiResults.NoSuccess<List<Result>>).message,
                Toast.LENGTH_SHORT
            ).show()
            searchViewModel.resetState()
        }

        is ApiResults.Success<List<Result>> -> {
            searchViewModel.setRecipes((searchState as ApiResults.Success<List<Result>>).data)
            Log.w("TAG", "Success")

            searchViewModel.resetState()
        }

        else -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)

            ) {
                items(recipes) {
                    RecipeApiCard(recipe = it, onClicRecipe = { result ->
                        onClicRecipe(result)
                    }, onDownload = {
                        onDownload(it)

                    })
                }
            }

        }
    }

    /*LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Adaptive(150.dp),
    verticalItemSpacing = 8.dp,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)

    ) {
    items(recipeList) { recipe ->
        RecipeCard(recipe) {
            onClicRecipe(it)
        }
    }
    }*/
}


@Composable
fun SearchRow(text: String, onClicSearch: () -> Unit, onRememberText: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClicSearch() }
        .background(color = Color(0xFFD6F5F7))) {
        Image(
            painter = painterResource(id = R.drawable.time_recent),
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = text, modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterVertically), fontSize = 18.sp, color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(id = R.drawable.baseline_arrow_outward_24),
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    onRememberText()
                })
    }
}

@Composable
fun RangeSliderExample(searchViewModel: SearchViewModel) {
    val sliderPosition by searchViewModel.servingRange.collectAsState()

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        RangeSlider(
            value = sliderPosition,
            steps = 50,
            onValueChange = { range ->
                searchViewModel.setMaxServings(range)
            },
            valueRange = 1f..50f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)

            },
        )
        Text(
            text = "${sliderPosition.start.toInt()} - ${sliderPosition.endInclusive.toInt()} ingredientes",
            color = Color(
                0xFF142EBE
            ),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontFamily = Fonts.poppins,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SliderMinimalExample(searchViewModel: SearchViewModel) {
    val sliderPosition by searchViewModel.maxReadyMin.collectAsState()
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Slider(
            valueRange = 5f..200f,
            value = sliderPosition,
            onValueChange = { searchViewModel.setMaxReadyMin(it) }
        )
        Text(
            text = "Máximo de ${sliderPosition.toInt()} minutos ",
            color = Color(
                0xFF142EBE
            ),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontFamily = Fonts.poppins,
            fontWeight = FontWeight.Normal
        )
    }
}