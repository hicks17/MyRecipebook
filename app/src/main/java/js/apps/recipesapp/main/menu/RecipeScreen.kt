package js.apps.recipesapp.main.menu

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.google.android.material.chip.ChipGroup
import js.apps.recipesapp.R
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.model.Recipe
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.utils.Fonts
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.RecipeVM

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun RecipeScreen(fabClick: () -> Unit,
                 recipeVM: RecipeVM,
                 onClicRecipe: (Recipe) -> Unit,
                 onClicDownload: (ResultEntity) -> Unit
) {
    val recipeList: List<Recipe> by recipeVM.recipeList.collectAsState()
    val downloadList: List<ResultEntity> by recipeVM.downloadedRecipes.collectAsState()
    val isFilterShown by recipeVM.isFilterShown.collectAsState()
    val selected by recipeVM._isFavChipSelected.collectAsState()
    val query by recipeVM.query.collectAsState()
    val isDownload by recipeVM.isDownloadSelected.collectAsState()
    var isUserPremium by remember { mutableStateOf(false) }

    if(selected){
        recipeVM.setFavoriteList()
    }else{
        recipeVM.updateList()
    }
    var selectedSide by remember { mutableStateOf(false) }

    val context = LocalContext.current
    isUserPremium = PremiumSharedPref(context).obtenerValorBooleano()
    var selectedDish by remember { mutableStateOf(false) }
    var selectedDessert by remember { mutableStateOf(false) }
    var selectedBreak by remember { mutableStateOf(false) }
    var selectedDinner by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf(false) }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {

            val (fab, myrecipes, filterBy, recipeLayout, filterMenu, banner, recipeLayoutDownloads, chips) = createRefs()


            Icon(painter = if(isFilterShown) painterResource(id = R.drawable.filter_off) else painterResource(
                id = R.drawable.baseline_filter_list_24
            ),
                contentDescription = "", modifier = Modifier
                    .constrainAs(filterBy) {
                        end.linkTo(parent.end, margin = 30.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                    }
                    .clickable { recipeVM.onFilterShown() }, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = context.getString(R.string.my_recipes),
                fontFamily = Fonts.poppins,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(myrecipes) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 20.dp)

                }, fontSize = 32.sp, color = MaterialTheme.colorScheme.onSurface
            )
            Icon(painter =  painterResource(id = R.drawable.add_recipe),
                contentDescription = "", modifier = Modifier.constrainAs(fab) {
                    end.linkTo(filterBy.start, margin = 10.dp)
                    top.linkTo(parent.top, margin = 10.dp)
                }.clickable {
                    fabClick()
                }, tint = MaterialTheme.colorScheme.primary)

            Row(modifier = Modifier.constrainAs(chips){
                top.linkTo(filterMenu.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 20.dp)
            }) {
                FilterChip(selected = !isDownload, onClick = {
                                        if (isDownload) {
                                            recipeVM.setDownloadSelected(false)
                                        }
                }, label = { Text(text = context.getString(R.string.my_recipes), style = Styles.filterChipStyle) }, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(selected = isDownload, onClick = {

                    if (!isDownload) {
                        recipeVM.setDownloadSelected(true)
                    }
                }, label = { Text(text = context.getString(R.string.downloads), style = Styles.filterChipStyle) }, shape = RoundedCornerShape(12.dp))
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                contentPadding = if (!isUserPremium) PaddingValues(
                    bottom = 100.dp) else PaddingValues(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .constrainAs(recipeLayout) {
                        top.linkTo(chips.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(start = 8.dp, end = 8.dp)

            ) {
                if (isDownload) {
                    items(downloadList){ recipe ->
                        RecipeApiCard(recipe = recipe, onClicRecipe = {
                                onClicDownload(it)
                        }) {
                            recipeVM.deleteDownload(recipe)
                        }
                    }
                }else {

                    items(recipeList) { recipe ->
                        RecipeCard(recipe, onClicRecipe = { onClicRecipe(recipe) }, onFavClicked = {
                            recipeVM.onFavClicked(it, recipe)
                        }, onDelete = { recipeVM.deleteRecipe(it) })
                    }
                }
            }


            FlowRow(
                modifier = Modifier
                    .constrainAs(filterMenu) {
                        top.linkTo(myrecipes.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                if (isFilterShown) {
                    CustomSearchView(search = query, onValueChange = {
                        recipeVM.query.value = it.lowercase()
                        recipeVM.searchByTitle()
                    })
                    FilterChip(
                        onClick = { recipeVM._isFavChipSelected.value = !selected },
                        label = {
                            Text(context.getString(R.string.favorites), style = Styles.filterChipStyle)
                        },
                        selected = selected,
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        }
                    )
                    FilterChip(selected = selectedSide, onClick = {
                        if (!selectedSide) {
                            recipeVM.onFilterSelected("Entrada")
                        } else {
                            recipeVM.onRemovedFilter("Entrada")
                        }
                        selectedSide = !selectedSide
                    },
                        label = { Text(text = context.getString(R.string.side), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedSide) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        })
                    FilterChip(selected = selectedDish, onClick = {
                        if (!selectedDish) {
                            recipeVM.onFilterSelected("Plato fuerte")
                        } else {
                            recipeVM.onRemovedFilter("Plato fuerte")
                        }
                        selectedDish = !selectedDish
                    },
                        label = { Text(text = context.getString(R.string.main_dish), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedDish) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else { null })
                    FilterChip(selected = selectedDessert, onClick = {
                        if (!selectedDessert) {
                            recipeVM.onFilterSelected("Postre")
                        } else {
                            recipeVM.onRemovedFilter("Postre")
                        }
                        selectedDessert = !selectedDessert
                    },
                        label = { Text(text = context.getString(R.string.desserts), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedDessert) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else { null })
                    FilterChip(selected = selectedBreak, onClick = {
                        if (!selectedBreak) {
                            recipeVM.onFilterSelected("Desayuno")
                        } else {
                            recipeVM.onRemovedFilter("Desayuno")
                        }
                        selectedBreak = !selectedBreak
                    },
                        label = { Text(text = context.getString(R.string.breakfast), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedBreak) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else { null })
                    FilterChip(selected = selectedMeal, onClick = {
                        if (!selectedMeal) {
                            recipeVM.onFilterSelected("Comida")
                        } else {
                            recipeVM.onRemovedFilter("Comida")
                        }
                        selectedMeal = !selectedMeal
                    },
                        label = { Text(text = context.getString(R.string.meal), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedMeal) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else { null })
                    FilterChip(selected = selectedDinner, onClick = {
                        if (!selectedDinner) {
                            recipeVM.onFilterSelected("Cena")
                        } else {
                            recipeVM.onRemovedFilter("Cena")
                        }
                        selectedDinner = !selectedDinner
                    },
                        label = { Text(text = context.getString(R.string.dinner), style = Styles.filterChipStyle) },
                        leadingIcon = if (selectedDinner) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else { null })
                }
            }
            BannerAd(adUnitId = "ca-app-pub-8489206541399786/2797594003", modifier = Modifier
                .constrainAs(banner) {
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeCard(recipe: Recipe, onClicRecipe: (Recipe) -> Unit,
               onFavClicked:(Boolean) -> Unit, onDelete: (Recipe) -> Unit) {
    var isLongPressed by remember { mutableStateOf(false) }
    var dpOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp,
    ), modifier = Modifier.padding(top = 8.dp)) {

            var isFav by remember { mutableStateOf(false) }

            isFav = recipe.isFav
            ConstraintLayout(
                modifier = Modifier

                    .background(Color.White)
                    .onSizeChanged {
                        itemHeight = with(density) { it.height.toDp() }
                    }
                    .indication(interactionSource, LocalIndication.current)
                    .pointerInput(true) {
                        detectTapGestures(
                            onPress = {
                                val tap = PressInteraction.Press(it)
                                interactionSource.emit(tap)
                                tryAwaitRelease()
                                interactionSource.emit(PressInteraction.Release(tap))
                            },
                            onTap = {
                                onClicRecipe(recipe)
                            },
                            onLongPress = {
                                isLongPressed = true
                                dpOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        )
                    }
                    .fillMaxSize()
            ) {
                val (image, title, fav, personas, tiempo, desc, point, personIcon, dropdwnMenu) = createRefs()
                DropdownMenu(expanded = isLongPressed, onDismissRequest = { isLongPressed = false }, modifier = Modifier
                    .constrainAs(dropdwnMenu) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                    }, offset = dpOffset.copy(
                    y = dpOffset.y - itemHeight
                )) {
                    DropdownMenuItem(text = { Text(text = if (isFav) context.getString(R.string.remove_favorite) else context.getString(R.string.add_favorite), style = Styles.recipeTitleStyle, fontSize = 14.sp) }, onClick = {
                        isFav = !isFav
                        onFavClicked(!isFav)
                    }, trailingIcon = { Image(painter = if (!isFav) painterResource(id = R.drawable.selected_fav) else painterResource(id = R.drawable.fav),
                        contentDescription = "")})
                    DropdownMenuItem(text = { Text(text = "Read full recipe", style = Styles.recipeTitleStyle, fontSize = 14.sp) }, onClick = {
                        isLongPressed = false
                        onClicRecipe(recipe) })
                    DropdownMenuItem(text = { Text(text = context.getString(R.string.delete), style = Styles.recipeTitleStyle, fontSize = 14.sp) }, onClick = {
                        isLongPressed = false
                        onDelete(recipe) }, trailingIcon = {
                        Image(painter = painterResource(id = R.drawable.delete), contentDescription = "")
                    })
                }
                recipe.image?.asImageBitmap()?.let {
                    Image(bitmap = it, contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(100.dp, 300.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                            }, contentScale = ContentScale.FillBounds)
                }
                Text(text = recipe.title,
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(image.bottom, margin = 4.dp)
                            start.linkTo(parent.start, margin = 3.dp)
                            end.linkTo(parent.end, margin = 3.dp)
                        })
                Text(text = recipe.desc,
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.constrainAs(desc) {
                        top.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        visibility = if (recipe.desc.isNotBlank()) {
                            Visibility.Visible
                        } else {
                            Visibility.Gone
                        }
                    })

                Image(painter = painterResource(id = R.drawable.circle), contentDescription = "",
                    modifier = Modifier.constrainAs(point) {
                        top.linkTo(tiempo.top)
                        bottom.linkTo(tiempo.bottom)
                        end.linkTo(tiempo.start, margin = 3.dp)
                    })
                Text(text = "${recipe.tiempo} min",
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(tiempo) {
                        top.linkTo(desc.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    })
                Text(text = recipe.personas.toString(),
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(personas) {
                        top.linkTo(desc.bottom, margin = 10.dp)
                        end.linkTo(point.start, margin = 4.dp)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    })
                Image(painter = painterResource(id = R.drawable.person_12), contentDescription = "",
                    modifier = Modifier.constrainAs(personIcon) {
                        top.linkTo(personas.top)
                        bottom.linkTo(personas.bottom)
                        end.linkTo(personas.start, margin = 3.dp)

                    })

                Image(painter = if (!isFav) painterResource(id = R.drawable.fav) else painterResource(
                    id = R.drawable.selected_fav
                ),
                    contentDescription = "",

                    modifier = Modifier
                        .constrainAs(fav) {
                            bottom.linkTo(parent.bottom, margin = 6.dp)
                            start.linkTo(parent.start, margin = 6.dp)

                        }
                        .clickable {
                            isFav = !isFav
                            onFavClicked(isFav)
                        })
            }
        }
    }







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchView(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .padding(20.dp)
            .clip(CircleShape)
            .background(Color(0XFF101921))
            .fillMaxWidth()

    ) {
        TextField(value = search,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0XFF101921),
                focusedPlaceholderColor = Color(0XFF888D91),
                focusedLeadingIconColor = Color(0XFF888D91),
                focusedTrailingIconColor = Color(0XFF888D91),
                focusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent, cursorColor = Color(0XFF070E14)
            ),
            textStyle =Styles.searchRecipeStyle,
            trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
            placeholder = { Text(text = context.getString(R.string.search_by_title) , fontFamily = Fonts.poppins, fontWeight = FontWeight.Light) }
        )
    }
}
