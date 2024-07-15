package js.apps.recipesapp.data.local

import android.content.Context
import js.apps.recipesapp.data.local.PremiumSharedPref.Premium.USER_MODE
import js.apps.recipesapp.data.local.PremiumSharedPref.Premium.USER_PREMIUM

class PremiumSharedPref (context: Context) {

    object Premium{
        const val USER_MODE = "user_mode"
        const val USER_PREMIUM = "is_premium"
    }
    private val sharedPreferences = context.getSharedPreferences(USER_MODE, Context.MODE_PRIVATE)

    fun makeUserPremium(valor: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(USER_PREMIUM, valor)
        editor.apply()
    }

    fun obtenerValorBooleano(): Boolean {
        return sharedPreferences.getBoolean(USER_PREMIUM, false)
    }

    // ... métodos similares para otros tipos de datos
}