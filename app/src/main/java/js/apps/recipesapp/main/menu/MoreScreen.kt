package js.apps.recipesapp.main.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import js.apps.recipesapp.R
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.NewRecipeVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MoreScreem(newRecipeVM: NewRecipeVM) {

        val localContext = LocalContext.current
        val isDialogShown by newRecipeVM.isDialogShown.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .blur(if (isDialogShown) 10.dp else 0.dp)
        ) {

            /*Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text =localContext.getString(R.string.settings),
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription = "",
                    modifier = Modifier.padding(top = 16.dp),
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }*/
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    newRecipeVM.changeIsDialogShown(true)
                }) {
                Text(
                    text = "Premium",
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
            HorizontalDivider(modifier = Modifier.padding(start = 12.dp, end = 12.dp))
            Row(modifier = Modifier.fillMaxWidth().clickable {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:jc.canedo16@gmail.com") // Reemplaza con tu correo
                    putExtra(Intent.EXTRA_SUBJECT, "Feedback of JS! App: RecipeBook") // Asunto opcional
                    putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo") // Cuerpo opcional
                    localContext.startActivity(this)
                }
            }) {
                Text(
                    text = "Feedback",
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Icon(
                    painter = painterResource(id = R.drawable.dialogo), contentDescription = "",
                    modifier = Modifier.padding(top = 16.dp),
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
            HorizontalDivider(modifier = Modifier.padding(start = 12.dp, end = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = localContext.getString(R.string.rate_the_app),
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Icon(
                    painter = painterResource(id = R.drawable.redirect), contentDescription = "",
                    modifier = Modifier.padding(top = 16.dp),
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
            /*HorizontalDivider(modifier = Modifier.padding(start = 12.dp, end = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = localContext.getString(R.string.about_us),
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }*/
            HorizontalDivider(modifier = Modifier.padding(start = 12.dp, end = 12.dp))
            Row(modifier = Modifier.fillMaxWidth().clickable {
                val url = "https://firebasestorage.googleapis.com/v0/b/recipesapp-7492c.appspot.com/o/recipebookpolicy.html?alt=media&token=9cb73573-c0b6-4a7f-9904-c81f40d89242"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                localContext.startActivity(intent)
            }){
                Text(
                    text = localContext.getString(R.string.privacy_policy),
                    Modifier.padding(12.dp),
                    style = Styles.recipeTitleStyle,
                    color = MaterialTheme.colorScheme.inversePrimary
                )

                Icon(
                    painter = painterResource(id = R.drawable.redirect), contentDescription = "",
                    modifier = Modifier.padding(top = 16.dp),
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }

            if (isDialogShown) {
                PayPremiumDialog(onDismiss = { newRecipeVM.changeIsDialogShown(false) }) {
                    onAccept(localContext)
                }
            }


        }
    }

 fun onAccept(context: Context) {
    val payInstance = Module(context)
     payInstance.startConnection()



}

@Composable
fun BannerAd(adUnitId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdUnitId(adUnitId)
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
