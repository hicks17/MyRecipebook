package js.apps.recipesapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryPurchasesParams
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.data.local.PremiumSharedPref.Premium.USER_MODE
import js.apps.recipesapp.data.local.PremiumSharedPref.Premium.USER_PREMIUM

@HiltAndroidApp
class RecipeApp : Application(){
    //private lateinit var billingClient: BillingClient

    //private lateinit var premiumSharedPref: PremiumSharedPref
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        /*premiumSharedPref = PremiumSharedPref(this)



        val pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(pendingPurchasesParams)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }
            // ... (manejo de conexión)

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    verificarEstadoPremium()
                }
            }
        })
        if (!premiumSharedPref.obtenerValorBooleano()) {
            MobileAds.initialize(this)
        }*/

    }

    /*private fun verificarEstadoPremium() {

        val esPremiumCache = premiumSharedPref.obtenerValorBooleano()

        if (!esPremiumCache){
            // Consulta a Google Play para verificar el estado premium
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP) // O SUBS si es una suscripción
                    .build()
            ) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val esPremium = purchases.any { purchase ->
                        purchase.products.contains("premium_payment") &&
                                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    }
                        premiumSharedPref.makeUserPremium(esPremium)

                    Toast.makeText(this, "Estado premium: $esPremium", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // ... (manejo de cambios en las compras, actualiza Room y SharedPreferences)

    }*/
}