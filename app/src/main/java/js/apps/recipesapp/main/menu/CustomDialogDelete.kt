package js.apps.recipesapp.main.menu

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import js.apps.recipesapp.R
import js.apps.recipesapp.utils.Styles

@Composable
fun CustomDialogDelete(
    onDismiss:() -> Unit,
    onAccept:() -> Unit
){
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        usePlatformDefaultWidth = false
    )) {
        (LocalView.current.parent as DialogWindowProvider)?.window?.setDimAmount(0f)
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp), shape = RoundedCornerShape(10.dp)) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = context.getString(R.string.sure_delete), Modifier.padding(start = 16.dp, end = 16.dp), style = Styles.newRecipeStyle)
                Spacer(modifier = Modifier.height(40.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
                    Button(onClick = { onAccept() } ) {
                        Text(text = context.getString(R.string.yes), style = Styles.newRecipeStyle,
                            color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    Button(onClick = { onDismiss() }) {
                        Text(text ="No", style = Styles.newRecipeStyle, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}