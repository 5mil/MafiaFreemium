package com.mafiagame.freemium.billing

import com.mafiagame.freemium.data.EconomyManager
import com.mafiagame.freemium.model.ShopCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock billing for local testing / previews when Google Play is not available.
 * Simulates successful purchases instantly so UI and economy can be tested.
 */
class MockBillingManager(
    private val economyManager: EconomyManager
) {
    private val _purchaseResult = MutableStateFlow<BillingManager.PurchaseResult?>(null)
    val purchaseResult: StateFlow<BillingManager.PurchaseResult?> = _purchaseResult.asStateFlow()

    /**
     * Simulate a successful purchase of the given product.
     * In the real app this is replaced by BillingManager.launchPurchase().
     */
    fun simulatePurchase(productId: String) {
        val product = ShopCatalog.getById(productId)
        if (product == null) {
            _purchaseResult.value = BillingManager.PurchaseResult.Error("Unknown product: $productId")
            return
        }

        val success = economyManager.applyPurchase(productId)
        _purchaseResult.value = if (success) {
            BillingManager.PurchaseResult.Success(productId)
        } else {
            BillingManager.PurchaseResult.Error("Could not apply purchase")
        }
    }

    /**
     * Simulate buying with Diamonds (in-app soft purchase).
     */
    fun simulateBuyWithDiamonds(productId: String) {
        val success = economyManager.buyWithGems(productId)
        _purchaseResult.value = if (success) {
            BillingManager.PurchaseResult.Success(productId)
        } else {
            BillingManager.PurchaseResult.Error("Not enough Diamonds or invalid product")
        }
    }

    fun clearResult() {
        _purchaseResult.value = null
    }
}