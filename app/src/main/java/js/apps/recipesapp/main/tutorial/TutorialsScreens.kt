package js.apps.recipesapp.main.tutorial

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import js.apps.recipesapp.R
import js.apps.recipesapp.utils.Styles


@Composable
fun FirstTutorialScreen(){

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)) {
        val (skip, next, navBar, tutorial, nextImage, decorBack, image) = createRefs()

        Image(painter = painterResource(id = R.drawable.test_screen),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
            }.width(250.dp).height(500.dp))
        Canvas(modifier = Modifier.wrapContentSize().constrainAs(decorBack){

            bottom.linkTo(parent.bottom, margin = 66.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }) {
            drawCircle(color = Color(0xFF47528B), radius = 800f)
        }

        Text(text = "Guarda y busca tus deliciosas recetas.",
            modifier = Modifier
                .constrainAs(tutorial) {

                    bottom.linkTo(parent.bottom, margin = 66.dp)
                    end.linkTo(parent.end)

                }
                .width(180.dp), style = Styles.recipeTitleStyle, fontSize = 32.sp)
        Image(painter = painterResource(id = R.drawable.next_24),
            contentDescription = "", modifier = Modifier.constrainAs(nextImage) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })
        Text(text = "Saltar", modifier = Modifier.constrainAs(skip) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)

        },
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
        Text(text = "Siguiente", modifier = Modifier.constrainAs(next) {

            end.linkTo(nextImage.start)
            bottom.linkTo(nextImage.bottom, margin = 4.dp)
        }, style = Styles.recipeInstructionStyle)
        Row(modifier = Modifier.constrainAs(navBar) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }){
            Image(painter = painterResource(id = R.drawable.circle),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
        }
}
}

@Preview(showBackground = true)
@Composable
fun SecondTutorialScreen(){

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan)) {
        val (skip, next, navBar, tutorial, nextImage, decorBack, image) = createRefs()

        Image(painter = painterResource(id = R.drawable.test_screen),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top, margin = 50.dp)
                end.linkTo(parent.end)
            }.width(250.dp).height(500.dp))
        Canvas(modifier = Modifier.wrapContentSize().constrainAs(decorBack){

            bottom.linkTo(parent.bottom, margin = 66.dp)
            start.linkTo(parent.start, margin = 16.dp)
        }) {
            drawCircle(color = Color(0xFFFF1018), radius = 800f)
        }

        Text(text = "Marca tus recetas favoritas y busca por título y tipo.",
            modifier = Modifier
                .constrainAs(tutorial) {

                    bottom.linkTo(parent.bottom, margin = 26.dp)
                    start.linkTo(parent.start, margin = 16.dp)

                }
                .width(180.dp), style = Styles.recipeTitleStyle, fontSize = 32.sp)
        Image(painter = painterResource(id = R.drawable.next_24),
            contentDescription = "", modifier = Modifier.constrainAs(nextImage) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })
        Text(text = "Saltar", modifier = Modifier.constrainAs(skip) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)

        },
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
        Text(text = "Siguiente", modifier = Modifier.constrainAs(next) {

            end.linkTo(nextImage.start)
            bottom.linkTo(nextImage.bottom, margin = 4.dp)
        }, style = Styles.recipeInstructionStyle)
        Row(modifier = Modifier.constrainAs(navBar) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }){
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
            Image(painter = painterResource(id = R.drawable.circle_white),
                contentDescription = "", modifier = Modifier.padding(end = 6.dp))
        }
}
}