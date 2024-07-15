package js.apps.recipesapp.main.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import js.apps.recipesapp.model.spoonacular.Poster
import js.apps.recipesapp.viewModel.SearchViewModel
import androidx.compose.ui.unit.toRect
import androidx.constraintlayout.compose.Dimension
import js.apps.recipesapp.R
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.utils.toIntRect

@Composable
fun SavedRecipeScreen(searchViewModel: SearchViewModel,onBack: () -> Unit, onClic: (Poster) -> Unit) {
    val savedRecipes by searchViewModel.savedRecipes.collectAsState()

    val context = LocalContext.current
    BackHandler {
        onBack()
    }
    searchViewModel.getSavedRecipes()
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                Icon(painter = painterResource(id = R.drawable.return_btn), contentDescription = "", modifier = Modifier.clickable {
                    onBack()
                }.padding(10.dp), tint = MaterialTheme.colorScheme.inverseSurface)
                Text(text = context.getString(R.string.my_saved_recipes), modifier = Modifier.padding(start = 10.dp, bottom = 22.dp),
                    style = Styles.recipeTitleStyle, color = MaterialTheme.colorScheme.inversePrimary)
            }

        }
        items(savedRecipes) {
            SavedOverView(it, onClic = {
                onClic(it)
            }, onDelete = {
                searchViewModel.deleteSaved(it.roomId!!)
            })
        }
    }
}

@Composable
fun SavedOverView(it: Poster, onClic: () -> Unit, onDelete: () -> Unit) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Card(onClick = { onClic() }, modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight()) {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)) {
            val (title, image, icon) = createRefs()
            Text(text = it.title, modifier = Modifier.constrainAs(title){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(icon.start)
                start.linkTo(image.end, margin = 8.dp)
                height = Dimension.wrapContent
                width = Dimension.fillToConstraints
            }, style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onSecondaryContainer, textAlign = TextAlign.Center)
            AsyncImage(model = it.image, modifier = Modifier
                .fillMaxHeight()
                .constrainAs(image) {
                    top.linkTo(parent.top)
                }, contentDescription = "")

            Box(modifier = Modifier.constrainAs(icon) {
                top.linkTo(parent.top, margin = 4.dp)
                end.linkTo(parent.end, margin = 4.dp)}) {

                IconButton(
                    onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.Black)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.share), style = Styles.filterChipStyle) },
                        trailingIcon = { Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = ""
                        )},
                        onClick = {
                            // Handle share action
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(context.getString(R.string.delete), style = Styles.filterChipStyle) },
                        trailingIcon = { Image(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = ""
                        )},
                        onClick = {
                            onDelete()
                            expanded = false
                        }
                    )
                }
            }
        }

    }

}
