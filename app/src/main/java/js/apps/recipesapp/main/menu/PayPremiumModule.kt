package js.apps.recipesapp.main.menu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import js.apps.recipesapp.data.local.PremiumSharedPref
import js.apps.recipesapp.utils.getActivityOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PayPremiumModule(val context: Context) {
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    private lateinit var productDetailsParamsList: List<BillingFlowParams.ProductDetailsParams>
    private lateinit var productDetailsResult: ProductDetailsResult
    private val productList = listOf(
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId("premium_payment")
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    )
    val params = QueryProductDetailsParams.newBuilder()
    val pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()

    private var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(pendingPurchasesParams)
        // Configure other settings.
        .build()

fun setBillingClient() {
    billingClient.startConnection(object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // The BillingClient is ready. You can query purchases here.
                comprarProductos()
            }
        }

        private fun comprarProductos() {
            Toast.makeText(context, "Ha comprado el producto", Toast.LENGTH_SHORT).show()
        }

        override fun onBillingServiceDisconnected() {
            // Try to restart the connection on the next request to
            // Google Play by calling the startConnection() method.
            Toast.makeText(
                context,
                "Se ha parado la conexion",
                Toast.LENGTH_SHORT
            ).show()
        }
    })

}

    suspend fun processPurchases(): List<ProductDetails>? {

        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        // leverage queryProductDetails Kotlin extension function
        productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }

        // Process the result.
        return productDetailsResult.productDetailsList


    }

    suspend fun launchBillingFlow(){

        productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(processPurchases()!!.get(0))
                // For One-time product, "setOfferToken" method shouldn't be called.
                // For subscriptions, to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        val activity = context.getActivityOrNull()

        // Launch the billing flow
        val billingResult =
            billingClient.launchBillingFlow(activity!!, billingFlowParams)
        Toast.makeText(context, "Comprado", Toast.LENGTH_SHORT).show()
    }
}

class Module(val context: Context) {

    private val productDetailsParamsList = mutableListOf<BillingFlowParams.ProductDetailsParams>()
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
               handlePurchase(purchases)
            }else if(billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
                Toast.makeText(context, "Ya has comprado esto", Toast.LENGTH_SHORT).show()
            }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
                Toast.makeText(context, "Compra cancelada", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Errorr", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handlePurchase(purchases: List<Purchase>) {
        for (purchase in purchases) {
            // Verifica el producto comprado y realiza acciones en consecuencia
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                val sku = purchase.products[0]
                if (sku == "premium_payment") {
                    // Realiza acciones para el producto premium
                    val userPf = PremiumSharedPref(context)
                    userPf.makeUserPremium(true)
                    Toast.makeText(context, "Premium purchased", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()

    private var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(pendingPurchasesParams)
        // Configure other settings.
        .build()

    fun retrieveProducts() {
        val productList: ArrayList<QueryProductDetailsParams.Product> = ArrayList()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("premium_payment")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult,
                                                                            skuDetailsList ->
            if (skuDetailsList.isNotEmpty()) {
                for (productDet in skuDetailsList) {
                    productDetailsParamsList.add(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                            .setProductDetails(productDet)
                            // For One-time product, "setOfferToken" method shouldn't be called.
                            // For subscriptions, to get an offer token, call ProductDetails.subscriptionOfferDetails()
                            // for a list of offers that are available to the user
                            .build()
                    )
                    Log.w("", "MYAPP-TEST----")
                    Log.w("", productDet.name)
                }

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                val activity = context.getActivityOrNull()

                // Launch the billing flow
                val billingResult =
                    billingClient.launchBillingFlow(activity!!, billingFlowParams)

                // Process list of matching products
            } else {

                Log.w("", "MYAPP-TEST---No product matches found")
                // No product matches found
            }
            // Process the result
        }
    }

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Connection successful
                    Log.w("", "MYAPP-TEST---SUCCESS")
                    // This is where you can start retrieving your products
                    retrieveProducts()
                } else {
                    // Connection failed
                }
            }

            override fun onBillingServiceDisconnected() {
                // Connection to billing service lost
                Log.w("", "MYAPP-TEST---DISCONNECTED")
            }
        })
    }
}