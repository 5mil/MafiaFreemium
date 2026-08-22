package com.mafiagame.freemium.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.mafiagame.freemium.data.EconomyManager
import com.mafiagame.freemium.model.ShopCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Google Play Billing integration for Mafia Freemium.
 *
 * Handles:
 * - Connecting to Google Play
 * - Querying product details (Diamonds, Hits, roles, Made Man, etc.)
 * - Launching the purchase flow
 * - Acknowledging purchases
 * - Applying successful purchases to the EconomyManager
 *
 * Completely ad-free. Pure microtransactions only.
 *
 * NOTE: This is production-ready structure. In the real app you must:
 * 1. Add the Billing dependency in build.gradle
 * 2. Create the products in Google Play Console with the exact product IDs
 * 3. Test with license testers
 */
class BillingManager(
    private val context: Context,
    private val economyManager: EconomyManager
) : PurchasesUpdatedListener {

    private var billingClient: BillingClient? = null

    private val _connectionState = MutableStateFlow(BillingConnectionState.DISCONNECTED)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState.asStateFlow()

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productDetails: StateFlow<List<ProductDetails>> = _productDetails.asStateFlow()

    private val _purchaseResult = MutableStateFlow<PurchaseResult?>(null)
    val purchaseResult: StateFlow<PurchaseResult?> = _purchaseResult.asStateFlow()

    enum class BillingConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }

    sealed class PurchaseResult {
        data class Success(val productId: String) : PurchaseResult()
        data class Error(val message: String) : PurchaseResult()
        object Cancelled : PurchaseResult()
        object Pending : PurchaseResult()
    }

    fun startConnection() {
        if (billingClient?.isReady == true) {
            _connectionState.value = BillingConnectionState.CONNECTED
            queryProducts()
            return
        }

        _connectionState.value = BillingConnectionState.CONNECTING

        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _connectionState.value = BillingConnectionState.CONNECTED
                    queryProducts()
                    queryExistingPurchases()
                } else {
                    _connectionState.value = BillingConnectionState.ERROR
                }
            }

            override fun onBillingServiceDisconnected() {
                _connectionState.value = BillingConnectionState.DISCONNECTED
            }
        })
    }

    fun endConnection() {
        billingClient?.endConnection()
        billingClient = null
        _connectionState.value = BillingConnectionState.DISCONNECTED
    }

    private fun queryProducts() {
        val client = billingClient ?: return

        val inAppIds = ShopCatalog.products
            .filter { !it.isSubscription }
            .map { it.id }

        val subIds = ShopCatalog.products
            .filter { it.isSubscription }
            .map { it.id }

        if (inAppIds.isNotEmpty()) {
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    inAppIds.map {
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(it)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    }
                )
                .build()

            client.queryProductDetailsAsync(params) { billingResult, detailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val current = _productDetails.value.toMutableList()
                    current.addAll(detailsList)
                    _productDetails.value = current
                }
            }
        }

        if (subIds.isNotEmpty()) {
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    subIds.map {
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(it)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    }
                )
                .build()

            client.queryProductDetailsAsync(params) { billingResult, detailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val current = _productDetails.value.toMutableList()
                    current.addAll(detailsList)
                    _productDetails.value = current
                }
            }
        }
    }

    fun launchPurchase(activity: Activity, productId: String) {
        val client = billingClient ?: run {
            _purchaseResult.value = PurchaseResult.Error("Billing not connected")
            return
        }

        val details = _productDetails.value.find { it.productId == productId }
            ?: run {
                _purchaseResult.value = PurchaseResult.Error("Product not found: $productId")
                return
            }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .apply {
                    details.subscriptionOfferDetails?.firstOrNull()?.let {
                        setOfferToken(it.offerToken)
                    }
                }
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val result = client.launchBillingFlow(activity, billingFlowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            _purchaseResult.value = PurchaseResult.Error("Failed to launch billing flow")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseResult.value = PurchaseResult.Cancelled
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                purchases?.forEach { handlePurchase(it) }
            }
            else -> {
                _purchaseResult.value = PurchaseResult.Error(
                    "Purchase failed: ${billingResult.debugMessage}"
                )
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            _purchaseResult.value = PurchaseResult.Pending
            return
        }

        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        if (!purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient?.acknowledgePurchase(acknowledgeParams) { result ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    applyPurchaseToEconomy(purchase)
                }
            }
        } else {
            applyPurchaseToEconomy(purchase)
        }
    }

    private fun applyPurchaseToEconomy(purchase: Purchase) {
        purchase.products.forEach { productId ->
            val success = economyManager.applyPurchase(productId)
            if (success) {
                _purchaseResult.value = PurchaseResult.Success(productId)
            } else {
                _purchaseResult.value = PurchaseResult.Error("Failed to apply $productId")
            }
        }
    }

    private fun queryExistingPurchases() {
        val client = billingClient ?: return

        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { handlePurchase(it) }
            }
        }

        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { handlePurchase(it) }
            }
        }
    }

    fun clearPurchaseResult() {
        _purchaseResult.value = null
    }
}