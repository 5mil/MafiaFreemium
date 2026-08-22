package com.mafiagame.freemium.data

import com.mafiagame.freemium.model.*

/**
 * Handles all freemium economy logic.
 * Completely ad-free. Monetization is pure microtransactions only
 * (Diamonds, Hits, roles, Made Man status, etc.).
 *
 * In a real app this would be backed by DataStore / Room + Google Play Billing.
 */
class EconomyManager(
    private val wallet: PlayerWallet = PlayerWallet()
) {
    fun getWallet(): PlayerWallet = wallet

    // --- Hits (Energy) ---

    fun canStartGame(): Boolean {
        refreshEnergyIfNeeded()
        return wallet.energy > 0 || wallet.isVip
    }

    fun consumeEnergyForGame(): Boolean {
        if (wallet.isVip) return true
        refreshEnergyIfNeeded()
        if (wallet.energy <= 0) return false
        wallet.energy -= 1
        return true
    }

    fun refreshEnergyIfNeeded(now: Long = System.currentTimeMillis()) {
        val oneDayMs = 24 * 60 * 60 * 1000L
        if (now - wallet.lastEnergyRefresh > oneDayMs) {
            wallet.energy = wallet.maxEnergy
            if (wallet.isVip) wallet.energy += 2
            wallet.lastEnergyRefresh = now
        }
    }

    fun addEnergy(amount: Int) {
        wallet.energy = (wallet.energy + amount).coerceAtMost(999)
    }

    // --- Currencies ---

    fun addCoins(amount: Int) {
        wallet.coins += amount
    }

    fun addGems(amount: Int) {
        wallet.gems += amount
    }

    fun spendGems(amount: Int): Boolean {
        if (wallet.gems < amount) return false
        wallet.gems -= amount
        return true
    }

    fun spendCoins(amount: Int): Boolean {
        if (wallet.coins < amount) return false
        wallet.coins -= amount
        return true
    }

    // --- Unlocks ---

    fun unlockRole(roleId: RoleId): Boolean {
        return wallet.unlockedRoles.add(roleId)
    }

    fun hasRole(roleId: RoleId): Boolean = roleId in wallet.unlockedRoles

    fun unlockAllPremiumRoles() {
        RoleCatalog.premiumRoles.forEach { wallet.unlockedRoles.add(it.id) }
    }

    // --- Purchases (called after successful Google Play Billing) ---

    fun applyPurchase(productId: String): Boolean {
        val product = ShopCatalog.getById(productId) ?: return false

        when (product.type) {
            ProductType.GEM_PACK -> {
                addGems(product.gemsGranted)
            }
            ProductType.ENERGY_PACK -> {
                addEnergy(product.energyGranted)
            }
            ProductType.ROLE_UNLOCK -> {
                product.roleUnlocked?.let { unlockRole(it) }
            }
            ProductType.ROLE_PACK -> {
                if (product.id == "roles_all_premium") unlockAllPremiumRoles()
            }
            ProductType.VIP_SUBSCRIPTION -> {
                wallet.isVip = true
                wallet.maxEnergy = 7
                addGems(100) // monthly grant example
            }
            ProductType.STARTER_PACK -> {
                addGems(product.gemsGranted)
                addEnergy(product.energyGranted)
                val premium = RoleCatalog.premiumRoles.random()
                unlockRole(premium.id)
            }
            ProductType.BATTLE_PASS -> {
                // Future: unlock battle pass track
            }
            ProductType.COSMETIC -> {
                // Future
            }
        }
        return true
    }

    // --- Soft currency purchases (spend Diamonds inside the app) ---

    fun buyWithGems(productId: String): Boolean {
        val gemCost = when (productId) {
            "energy_5" -> 50
            "energy_20" -> 150
            "energy_50" -> 300
            "energy_unlimited_24h" -> 200
            "role_bodyguard" -> 150
            "role_jester" -> 120
            "role_serial_killer" -> 200
            "role_mayor" -> 100
            "role_vigilante" -> 180
            "role_witch" -> 220
            "role_cupid" -> 160
            "role_hunter" -> 140
            "role_fool" -> 130
            "role_arsonist" -> 250
            "roles_all_premium" -> 800
            else -> return false
        }
        if (!spendGems(gemCost)) return false
        return applyPurchase(productId)
    }
}