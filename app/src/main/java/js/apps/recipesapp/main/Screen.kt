package js.apps.recipesapp.main

sealed class Screen(val route:String){
    data object RecipeScreen:Screen("Recetas")
    data object SearchScreen:Screen("Buscar")
    data object MoreScreen:Screen("Más")
    data object NewRecipeScreen:Screen("Nueva receta")
    data object onDetailRecipeScreen:Screen("RecipeOnDetail")
}

sealed class Tutorial(val route:String){
    data object Tutorial1stScreen:Tutorial("Tutorial")
    data object Tutorial2nssScreen:Tutorial("Tutorial2")
    data object Tutorial3rdScreen:Tutorial("Tutorial3")
}
