package js.apps.recipesapp.main.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import js.apps.recipesapp.R
import js.apps.recipesapp.utils.Styles


@Composable
fun PayPremiumDialog(
    onDismiss:() -> Unit,
    onAccept:() -> Unit
){
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        usePlatformDefaultWidth = false
    )
    ) {
        (LocalView.current.parent as DialogWindowProvider)?.window?.setDimAmount(0f)
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp), shape = RoundedCornerShape(10.dp)
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),

                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = context.getString(R.string.premium_offer), Modifier.padding(start = 16.dp, end = 16.dp),
                    style = Styles.newRecipeStyle, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Spacer(modifier = Modifier.height(40.dp))

                    Button(onClick = { onAccept() }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ) ) {
                        Text(text =context.getString(R.string.buy_premium), style = Styles.recipeInstructionTitleStyle, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        Image(
                            painter = painterResource(id = R.drawable.baseline_shopping_cart_24),
                            contentDescription = ""
                        )
                    }

                Spacer(modifier = Modifier.height(20.dp))
                }

        }
    }
}