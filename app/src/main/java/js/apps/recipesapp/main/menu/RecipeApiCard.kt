package js.apps.recipesapp.main.menu

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import coil.compose.AsyncImage
import js.apps.recipesapp.R
import js.apps.recipesapp.model.ResultEntity
import js.apps.recipesapp.model.testModel.Result
import js.apps.recipesapp.utils.Fonts
import js.apps.recipesapp.utils.Styles

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeApiCard(recipe: Result, onClicRecipe: (Result) -> Unit,
                  onDownload: () -> Unit
                  //sharedTransitionScope: SharedTransitionScope,
                  //animatedVisibilityScope: AnimatedVisibilityScope
){
    var isLongPressed by remember { mutableStateOf(false) }
    var dpOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

        Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ), onClick = { onClicRecipe(recipe) }, modifier = Modifier.padding(top = 8.dp)
        ) {

            ConstraintLayout(
                modifier = Modifier

                    .background(Color.White)
                    .onSizeChanged {
                        itemHeight = with(density) { it.height.toDp() }
                    }.indication(interactionSource, LocalIndication.current).pointerInput(true){
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
                val (image, title, fav, personas, tiempo, desc, point, personIcon, cheap, dropDownMenu) = createRefs()

                DropdownMenu(expanded = isLongPressed, onDismissRequest = { isLongPressed = false }, modifier = Modifier.constrainAs(dropDownMenu){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }, offset = dpOffset.copy(
                    y = dpOffset.y - itemHeight
                )) {
                    DropdownMenuItem(text = { Text(text = context.getString(R.string.read_full), style = Styles.recipeTitleStyle, fontSize = 20.sp) }, onClick = {
                        isLongPressed = false
                        onClicRecipe(recipe) })
                    DropdownMenuItem(text = { Text(text = context.getString(R.string.dowmload), style = Styles.recipeTitleStyle, fontSize = 20.sp) }, onClick = {
                        isLongPressed = false
                        onDownload() }, trailingIcon = {
                        Image(painter = painterResource(id = R.drawable.descargar), contentDescription = "")
                    })
                }
                AsyncImage(
                    model = recipe.image, contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                        }/*
                        .sharedElement(
                            rememberSharedContentState(key = "image-${recipe.id}"),
                                animatedVisibilityScope
                        )*/, contentScale = ContentScale.FillWidth
                )

                Text(text = recipe.title,
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(image.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    })
                Text(text = recipe.dishTypes.toString().replace("[", "").replace("]",""),
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.constrainAs(desc) {
                        top.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 6.dp)
                        end.linkTo(parent.end, margin = 6.dp)
                        width = Dimension.fillToConstraints
                    })

                Image(painter = painterResource(id = R.drawable.circle), contentDescription = "",
                    modifier = Modifier.constrainAs(point) {
                        top.linkTo(tiempo.top)
                        bottom.linkTo(tiempo.bottom)
                        end.linkTo(tiempo.start, margin = 3.dp)
                    })
                Text(text = "${recipe.readyInMinutes} min",
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(tiempo) {
                        top.linkTo(desc.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    })
                Text(text = recipe.servings.toString(),
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
                Image(painter = painterResource(id = R.drawable.planta),
                    contentDescription = "",

                    modifier = Modifier.constrainAs(fav) {
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 6.dp)
                        visibility = if (recipe.vegetarian) {
                            Visibility.Visible
                        } else {
                            Visibility.Gone
                        }
                    })

                if (recipe.cheap) {
                    Image(painter = painterResource(id = R.drawable.cheap),
                        contentDescription = "",

                        modifier = Modifier.constrainAs(cheap) {
                            bottom.linkTo(parent.bottom, margin = 6.dp)
                            start.linkTo(fav.start, margin = 6.dp)

                        })
                }
            }
        }
    }

@Composable
fun RecipeApiCard(recipe: ResultEntity, onClicRecipe: (ResultEntity) -> Unit,
                  onRemoveDownload: () -> Unit
                  //sharedTransitionScope: SharedTransitionScope,
                  //animatedVisibilityScope: AnimatedVisibilityScope
){
    var isLongPressed by remember { mutableStateOf(false) }
    var dpOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }

        Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ), onClick = { onClicRecipe(recipe) }, modifier = Modifier.padding(top = 8.dp)
        ) {

            ConstraintLayout(
                modifier = Modifier

                    .background(Color.White)
                    .onSizeChanged {
                        itemHeight = with(density) { it.height.toDp() }
                    }.indication(interactionSource, LocalIndication.current).pointerInput(true){
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
                val (image, title, fav, personas, tiempo, desc, point, personIcon, cheap, dropDownMenu) = createRefs()

                DropdownMenu(expanded = isLongPressed, onDismissRequest = { isLongPressed = false }, modifier = Modifier.constrainAs(dropDownMenu){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }.background(Color.White), offset = dpOffset.copy(
                    y = dpOffset.y - itemHeight
                )) {
                    DropdownMenuItem(text = { Text(text = "Read full recipe", style = Styles.recipeTitleStyle, fontSize = 20.sp) }, onClick = {
                        isLongPressed = false
                        onClicRecipe(recipe) })
                    DropdownMenuItem(text = { Text(text = "Remove download", style = Styles.recipeTitleStyle, fontSize = 20.sp) }, onClick = {
                        isLongPressed = false
                        onRemoveDownload() }, trailingIcon = {
                        Image(painter = painterResource(id = R.drawable.download_off), contentDescription = "")
                    })
                }
                AsyncImage(
                    model = recipe.image, contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                        }/*
                        .sharedElement(
                            rememberSharedContentState(key = "image-${recipe.id}"),
                                animatedVisibilityScope
                        )*/, contentScale = ContentScale.FillWidth
                )

                Text(text = recipe.title,
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(image.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    })
                Text(text = recipe.dishTypes.replace("[", "").replace("]",""),
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.constrainAs(desc) {
                        top.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 6.dp)
                        end.linkTo(parent.end, margin = 6.dp)
                        width = Dimension.fillToConstraints
                    })

                Image(painter = painterResource(id = R.drawable.circle), contentDescription = "",
                    modifier = Modifier.constrainAs(point) {
                        top.linkTo(tiempo.top)
                        bottom.linkTo(tiempo.bottom)
                        end.linkTo(tiempo.start, margin = 3.dp)
                    })
                Text(text = "${recipe.readyInMinutes} min",
                    fontFamily = Fonts.poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(tiempo) {
                        top.linkTo(desc.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    })
                Text(text = recipe.servings.toString(),
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
                Image(painter = painterResource(id = R.drawable.planta),
                    contentDescription = "",

                    modifier = Modifier.constrainAs(fav) {
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 6.dp)
                        visibility = if (recipe.vegetarian) {
                            Visibility.Visible
                        } else {
                            Visibility.Gone
                        }
                    })

                if (recipe.cheap) {
                    Image(painter = painterResource(id = R.drawable.cheap),
                        contentDescription = "",

                        modifier = Modifier.constrainAs(cheap) {
                            bottom.linkTo(parent.bottom, margin = 6.dp)
                            start.linkTo(fav.start, margin = 6.dp)

                        })
                }
            }
        }
    }
