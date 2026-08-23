package com.mafiagame.freemium.data

import com.mafiagame.freemium.model.*

/**
 * Handles all freemium economy logic.
 * Completely ad-free. Monetization is pure microtransactions only.
 *
 * When a [WalletRepository] is provided, every change is persisted via DataStore
 * so progress survives app restarts. Without it, the manager stays fully in-memory
 * (useful for unit tests and previews).
 */
class EconomyManager(
    private val repository: WalletRepository? = null,
    initialWallet: PlayerWallet = PlayerWallet()
) {
    private var wallet: PlayerWallet = initialWallet

    fun getWallet(): PlayerWallet = wallet

    suspend fun loadFromDisk() {
        repository?.let {
            wallet = it.getWallet()
            refreshEnergyIfNeeded()
            it.saveWallet(wallet)
        }
    }

    private suspend fun persist() {
        repository?.saveWallet(wallet)
    }

    fun canStartGame(): Boolean {
        refreshEnergyIfNeeded()
        return wallet.energy > 0 || wallet.isVip
    }

    suspend fun consumeEnergyForGame(): Boolean {
        if (wallet.isVip) return true
        refreshEnergyIfNeeded()
        if (wallet.energy <= 0) return false
        wallet.energy -= 1
        persist()
        return true
    }

    fun consumeEnergyForGameSync(): Boolean {
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

    suspend fun addEnergy(amount: Int) {
        wallet.energy = (wallet.energy + amount).coerceAtMost(999)
        persist()
    }

    fun addEnergySync(amount: Int) {
        wallet.energy = (wallet.energy + amount).coerceAtMost(999)
    }

    suspend fun addCoins(amount: Int) {
        wallet.coins += amount
        persist()
    }

    fun addCoinsSync(amount: Int) {
        wallet.coins += amount
    }

    suspend fun addGems(amount: Int) {
        wallet.gems += amount
        persist()
    }

    fun addGemsSync(amount: Int) {
        wallet.gems += amount
    }

    suspend fun spendGems(amount: Int): Boolean {
        if (wallet.gems < amount) return false
        wallet.gems -= amount
        persist()
        return true
    }

    fun spendGemsSync(amount: Int): Boolean {
        if (wallet.gems < amount) return false
        wallet.gems -= amount
        return true
    }

    suspend fun spendCoins(amount: Int): Boolean {
        if (wallet.coins < amount) return false
        wallet.coins -= amount
        persist()
        return true
    }

    suspend fun unlockRole(roleId: RoleId): Boolean {
        val added = wallet.unlockedRoles.add(roleId)
        if (added) persist()
        return added
    }

    fun unlockRoleSync(roleId: RoleId): Boolean {
        return wallet.unlockedRoles.add(roleId)
    }

    fun hasRole(roleId: RoleId): Boolean = roleId in wallet.unlockedRoles

    suspend fun unlockAllPremiumRoles() {
        RoleCatalog.premiumRoles.forEach { wallet.unlockedRoles.add(it.id) }
        persist()
    }

    fun unlockAllPremiumRolesSync() {
        RoleCatalog.premiumRoles.forEach { wallet.unlockedRoles.add(it.id) }
    }

    suspend fun applyPurchase(productId: String): Boolean {
        val product = ShopCatalog.getById(productId) ?: return false
        when (product.type) {
            ProductType.GEM_PACK -> addGems(product.gemsGranted)
            ProductType.ENERGY_PACK -> addEnergy(product.energyGranted)
            ProductType.ROLE_UNLOCK -> product.roleUnlocked?.let { unlockRole(it) }
            ProductType.ROLE_PACK -> if (product.id == "roles_all_premium") unlockAllPremiumRoles()
            ProductType.VIP_SUBSCRIPTION -> {
                wallet.isVip = true
                wallet.maxEnergy = 7
                addGems(100)
            }
            ProductType.STARTER_PACK -> {
                addGems(product.gemsGranted)
                addEnergy(product.energyGranted)
                unlockRole(RoleCatalog.premiumRoles.random().id)
            }
            ProductType.BATTLE_PASS -> {}
            ProductType.COSMETIC -> {}
        }
        return true
    }

    fun applyPurchaseSync(productId: String): Boolean {
        val product = ShopCatalog.getById(productId) ?: return false
        when (product.type) {
            ProductType.GEM_PACK -> addGemsSync(product.gemsGranted)
            ProductType.ENERGY_PACK -> addEnergySync(product.energyGranted)
            ProductType.ROLE_UNLOCK -> product.roleUnlocked?.let { unlockRoleSync(it) }
            ProductType.ROLE_PACK -> if (product.id == "roles_all_premium") unlockAllPremiumRolesSync()
            ProductType.VIP_SUBSCRIPTION -> {
                wallet.isVip = true
                wallet.maxEnergy = 7
                addGemsSync(100)
            }
            ProductType.STARTER_PACK -> {
                addGemsSync(product.gemsGranted)
                addEnergySync(product.energyGranted)
                unlockRoleSync(RoleCatalog.premiumRoles.random().id)
            }
            else -> {}
        }
        return true
    }

    suspend fun buyWithGems(productId: String): Boolean {
        val gemCost = gemCostFor(productId) ?: return false
        if (!spendGems(gemCost)) return false
        return applyPurchase(productId)
    }

    fun buyWithGemsSync(productId: String): Boolean {
        val gemCost = gemCostFor(productId) ?: return false
        if (!spendGemsSync(gemCost)) return false
        return applyPurchaseSync(productId)
    }

    private fun gemCostFor(productId: String): Int? = when (productId) {
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
        else -> null
    }
}