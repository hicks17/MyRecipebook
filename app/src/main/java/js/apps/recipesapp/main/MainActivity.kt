package js.apps.recipesapp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import js.apps.recipesapp.R
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.main.menu.CreateRecipeScreen
import js.apps.recipesapp.main.menu.MoreScreem
import js.apps.recipesapp.main.menu.RecipeOnDetailScreen
import js.apps.recipesapp.main.menu.RecipeScreen
import js.apps.recipesapp.main.menu.SavedRecipeScreen
import js.apps.recipesapp.main.menu.SearchScreen
import js.apps.recipesapp.main.menu.WebRecipeOnDetail
import js.apps.recipesapp.main.menu.WebRecipeOnDetailDownload
import js.apps.recipesapp.ui.theme.RecipesAppTheme
import js.apps.recipesapp.utils.Styles
import js.apps.recipesapp.viewModel.NewRecipeVM
import js.apps.recipesapp.viewModel.RecipeVM
import js.apps.recipesapp.viewModel.SearchViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var interstitialAdSearch: InterstitialAd? = null
    private lateinit var navController: NavHostController
    private lateinit var recipesNavController: NavHostController
    private lateinit var searchNavController: NavHostController
    private lateinit var moreNavController: NavHostController

    private val recipeVM: RecipeVM by viewModels()
    private val newRecipeVM: NewRecipeVM by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition", "UnrememberedGetBackStackEntry",
        "SourceLockedOrientationActivity"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            RecipesAppTheme {

                val isUserPremium = PremiumSharedPref(this).obtenerValorBooleano()
                if (!isUserPremium) {
                    LaunchedEffect(Unit) {
                        val adRequest = AdRequest.Builder().build()
                        InterstitialAd.load(
                            this@MainActivity,
                            "ca-app-pub-8489206541399786/8590359620",
                            adRequest,
                            object : InterstitialAdLoadCallback() {
                                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                    interstitialAdSearch = interstitialAd
                                }

                                // ... (manejo de errores)
                            }
                        )
                    }
                }else{
                    searchViewModel.makeUserPremium()
                }
                // A surface container using the 'background' color from the theme
                navController = rememberNavController()
                recipesNavController = rememberNavController()
                searchNavController = rememberNavController()
                moreNavController = rememberNavController()


                val items = listOf(
                    BottomNavItem(
                        title = getString(R.string.recipes),
                        selectedIcon = R.drawable.recipebook,
                        unselectedIcon = R.drawable.recipebook

                    ), BottomNavItem(
                        title = getString(R.string.search),
                        selectedIcon = R.drawable.search,
                        unselectedIcon = R.drawable.search

                    ), BottomNavItem(
                        title = getString(R.string.more),
                        selectedIcon = R.drawable.baseline_more_horiz_24,
                        unselectedIcon = R.drawable.baseline_more_horiz_24

                    )
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Surface(modifier = Modifier.fillMaxSize()) {

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {

                                            selectedItemIndex = index
                                            when (index) {
                                                0 -> {
                                                    when(recipeVM.recipeUiState.value){
                                                        0 -> {
                                                            navController.navigate(Screen.RecipeScreen.route)
                                                        }
                                                        1 -> {
                                                            navController.navigate(Screen.NewRecipeScreen.route)
                                                        }
                                                        2 -> {
                                                            navController.navigate(Screen.onDetailRecipeScreen.route)
                                                        }
                                                        3 -> {
                                                            navController.navigate("DownloadOnDetail")
                                                        }
                                                    }
                                                }
                                                1 -> {
                                                    when(searchViewModel.searchUiStateFlow.value){
                                                        0 -> {
                                                           navController.navigate(Screen.SearchScreen.route)
                                                        }
                                                        1 -> {
                                                            navController.navigate("savedRecipes")
                                                        }
                                                        2 -> {
                                                            navController.navigate("webapi")
                                                        }
                                                    }
                                                }
                                                2 -> {
                                                    navController.navigate(Screen.MoreScreen.route)
                                                }
                                            }

                                        },
                                        label = {
                                            Text(text = item.title, style = Styles.recipeInstructionStyle, color = MaterialTheme.colorScheme.onSurface)
                                        },
                                        icon = {
                                            Icon(
                                                painter = if (index == selectedItemIndex) {
                                                    painterResource(id = item.selectedIcon)
                                                } else {
                                                    painterResource(id = item.unselectedIcon)
                                                }, contentDescription = item.title
                                            )

                                        })
                                }
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.RecipeScreen.route,
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                        ) {


                            composable(Screen.RecipeScreen.route) {

                                RecipeScreen(recipeVM = recipeVM, fabClick = {
                                    recipeVM.setUiState(1)
                                    navController.navigate(Screen.NewRecipeScreen.route)/*{
                                        popUpTo(Screen.RecipeScreen.route){
                                            inclusive = true
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }*/

                                }, onClicRecipe = {
                                    recipeVM.setUiState(2)
                                    recipeVM.setCurrentRecipe(it)
                                    newRecipeVM.setValues(it)
                                    navController.navigate(Screen.onDetailRecipeScreen.route)/*{

                                            popUpTo(Screen.RecipeScreen.route){
                                                inclusive = true
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true

                                    }*/
                                }, onClicDownload = {
                                    recipeVM.setUiState(3)
                                    recipeVM.setDownloadRecipe(it)
                                    navController.navigate("DownloadOnDetail")
                                })
                            }
                            composable("DownloadOnDetail") {
                                WebRecipeOnDetailDownload(recipeVM) {
                                    recipeVM.setUiState(0)
                                    navController.navigate(Screen.RecipeScreen.route){
                                        popUpTo(Screen.RecipeScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            }
                            composable(Screen.NewRecipeScreen.route) {

                                CreateRecipeScreen(newRecipeVM, onBack = {
                                    recipeVM.setUiState(0)
                                    navController.navigate(Screen.RecipeScreen.route){
                                        popUpTo(Screen.RecipeScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }, goToDetail = {
                                    recipeVM.setUiState(2)
                                    recipeVM.setLastRecipe()

                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.added),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    newRecipeVM.clearRecipe()
                                    newRecipeVM.clearFields()

                                    newRecipeVM.setValues(it)
                                    navController.popBackStack()
                                    navController.navigate(Screen.onDetailRecipeScreen.route)/*{
                                        popUpTo(Screen.RecipeScreen.route){
                                            inclusive = true
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }*/


                                }, recipeVM = recipeVM){
                                    navController.navigate(Screen.MoreScreen.route)
                                    newRecipeVM.changeIsDialogShown(true)

                                }
                            }

                            composable(Screen.onDetailRecipeScreen.route) {

                                RecipeOnDetailScreen(recipeVM, onBack = {

                                    recipeVM.setUiState(0)
                                    if (newRecipeVM.isEditMode.value) {
                                        newRecipeVM.isEditMode()
                                    }
                                    newRecipeVM.clearFields()
                                    recipeVM.updateList()
                                    navController.navigate(Screen.RecipeScreen.route){
                                        popUpTo(Screen.RecipeScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }, onEditRecipe = {
                                    newRecipeVM.isEditMode()
                                    recipeVM.updateRecipe(it)
                                }, newRecipeVM)
                            }

                            composable(Screen.SearchScreen.route) {

                                SearchScreen(searchViewModel, onClicRecipe = { recipe ->
                                    if(!PremiumSharedPref(this@MainActivity).obtenerValorBooleano()){
                                        interstitialAdSearch?.show(this@MainActivity as Activity)
                                    }
                                    interstitialAdSearch?.show(this@MainActivity as Activity)
                                    searchViewModel.setRecipe(recipe)
                                    searchViewModel.setAccessedBySearch(true)
                                    navController.navigate("webapi"){
                                        popUpTo(Screen.SearchScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                    searchViewModel.setSearchUiState(2)
                                }, onClicSaved = {
                                    navController.navigate("savedRecipes")
                                    searchViewModel.setSearchUiState(1)
                                }, onDownload = {
                                    recipeVM.onNewDownload(it.toDownload())
                                }
                                ){
                                    navController.navigate(Screen.MoreScreen.route)
                                    newRecipeVM.changeIsDialogShown(true)

                                }
                            }
                            composable("webapi", enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Up,
                                    tween(700)
                                )
                            }, exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Down,
                                    tween(700)
                                )
                            }) {

                                WebRecipeOnDetail(
                                    searchViewModel = searchViewModel,
                                    onBack = {
                                             handleBack()
                                    },
                                    //sharedTransitionScope = this@SharedTransitionLayout,
                                    //animatedVisibilityScope = this@composable
                                )
                            }

                            composable("savedRecipes", enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Start,
                                    tween(500)
                                )
                            }, exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.End,
                                    tween(500)
                                )
                            }) {

                                SavedRecipeScreen(searchViewModel = searchViewModel, onBack = {
                                    navController.navigate(Screen.SearchScreen.route){
                                        popUpTo(Screen.SearchScreen.route){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                    searchViewModel.setSearchUiState(0)
                                }) {
                                    searchViewModel.setRecipeById(it.id)
                                    navController.navigate("webapi")
                                    searchViewModel.setSearchUiState(2)
                                }
                            }



                            composable(Screen.MoreScreen.route) {
                                MoreScreem(newRecipeVM)
                            }
                        }
                    }


                }


            }
        }
    }



    private fun handleBack() {
        if(searchViewModel.accessedBySearch.value){

            navController.navigate(Screen.SearchScreen.route){
                popUpTo(Screen.SearchScreen.route){
                    inclusive = true
                }
                launchSingleTop = true
            }
            searchViewModel.setSearchUiState(0)
        }else{
            navController.navigate("savedRecipes"){
                popUpTo(Screen.SearchScreen.route){
                    inclusive = true
                }
                launchSingleTop = true
            }
            searchViewModel.setSearchUiState(1)
        }
        searchViewModel.setAccessedBySearch(false)
    }


    @Composable
    fun SearchNavGraph(navController: NavHostController) {


        NavHost(navController = navController, startDestination = Screen.SearchScreen.route) {


        }

    }

    @Composable
    fun RecipesNavGraph(navController: NavHostController) {

        NavHost(navController = navController, startDestination = Screen.RecipeScreen.route) {

        }
    }

    @Composable
    inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
        val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
        val parentEntry = remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
        return hiltViewModel(parentEntry)
    }

}


