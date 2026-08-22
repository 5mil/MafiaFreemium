package com.mafiagame.freemium.model

/**
 * Core freemium economy models.
 * Soft currency = Coins (earned by playing)
 * Hard currency = Gems (bought with real money)
 * Energy limits how many games free users can start per day.
 */

data class PlayerWallet(
    var coins: Int = 0,
    var gems: Int = 0,
    var energy: Int = 5,                 // current energy
    var maxEnergy: Int = 5,              // free users
    var lastEnergyRefresh: Long = 0L,    // epoch millis for daily reset
    var isVip: Boolean = false,
    var adsRemoved: Boolean = false,
    var unlockedRoles: MutableSet<RoleId> = mutableSetOf(
        RoleId.VILLAGER, RoleId.MAFIA, RoleId.DOCTOR, RoleId.DETECTIVE
    )
)

enum class ProductType {
    GEM_PACK,
    ENERGY_PACK,
    ROLE_UNLOCK,
    ROLE_PACK,
    COSMETIC,
    REMOVE_ADS,
    VIP_SUBSCRIPTION,
    BATTLE_PASS,
    STARTER_PACK
}

data class ShopProduct(
    val id: String,                      // Google Play product ID
    val title: String,
    val description: String,
    val type: ProductType,
    val priceUsd: String,                // display only, real price from Play Billing
    val gemsGranted: Int = 0,
    val energyGranted: Int = 0,
    val coinsGranted: Int = 0,
    val roleUnlocked: RoleId? = null,
    val isConsumable: Boolean = true,    // true = can buy many times
    val isSubscription: Boolean = false
)

object ShopCatalog {
    val products = listOf(
        // Gem packs
        ShopProduct("gems_100", "100 Gems", "Small gem pack", ProductType.GEM_PACK, "$0.99", gemsGranted = 100),
        ShopProduct("gems_500", "550 Gems", "Popular pack (+10% bonus)", ProductType.GEM_PACK, "$4.99", gemsGranted = 550),
        ShopProduct("gems_1200", "1400 Gems", "Best value mid-tier (+16%)", ProductType.GEM_PACK, "$9.99", gemsGranted = 1400),
        ShopProduct("gems_2500", "3000 Gems", "Big pack (+20%)", ProductType.GEM_PACK, "$19.99", gemsGranted = 3000),
        ShopProduct("gems_6500", "8000 Gems", "Whale pack (+23%)", ProductType.GEM_PACK, "$49.99", gemsGranted = 8000),
        ShopProduct("gems_14000", "18000 Gems", "Ultimate pack (+28%)", ProductType.GEM_PACK, "$99.99", gemsGranted = 18000),

        // Energy
        ShopProduct("energy_5", "5 Energy", "Quick top-up", ProductType.ENERGY_PACK, "50 Gems", energyGranted = 5),
        ShopProduct("energy_20", "20 Energy", "Good value", ProductType.ENERGY_PACK, "150 Gems", energyGranted = 20),
        ShopProduct("energy_50", "50 Energy", "Stock up", ProductType.ENERGY_PACK, "300 Gems", energyGranted = 50),
        ShopProduct("energy_unlimited_24h", "Unlimited Energy 24h", "Play as much as you want for a day", ProductType.ENERGY_PACK, "$1.99 / 200 Gems", energyGranted = 999),

        // Individual premium roles
        ShopProduct("role_bodyguard", "Unlock Bodyguard", "Protect + counter-attack", ProductType.ROLE_UNLOCK, "150 Gems", roleUnlocked = RoleId.BODYGUARD, isConsumable = false),
        ShopProduct("role_jester", "Unlock Jester", "Win if lynched", ProductType.ROLE_UNLOCK, "120 Gems", roleUnlocked = RoleId.JESTER, isConsumable = false),
        ShopProduct("role_serial_killer", "Unlock Serial Killer", "Independent killer", ProductType.ROLE_UNLOCK, "200 Gems", roleUnlocked = RoleId.SERIAL_KILLER, isConsumable = false),
        ShopProduct("role_mayor", "Unlock Mayor", "Double vote power", ProductType.ROLE_UNLOCK, "100 Gems", roleUnlocked = RoleId.MAYOR, isConsumable = false),
        ShopProduct("role_vigilante", "Unlock Vigilante", "Day-time shot", ProductType.ROLE_UNLOCK, "180 Gems", roleUnlocked = RoleId.VIGILANTE, isConsumable = false),
        ShopProduct("role_witch", "Unlock Witch", "Poison & save potions", ProductType.ROLE_UNLOCK, "220 Gems", roleUnlocked = RoleId.WITCH, isConsumable = false),
        ShopProduct("role_cupid", "Unlock Cupid", "Link two lovers", ProductType.ROLE_UNLOCK, "160 Gems", roleUnlocked = RoleId.CUPID, isConsumable = false),
        ShopProduct("role_hunter", "Unlock Hunter", "Revenge kill on death", ProductType.ROLE_UNLOCK, "140 Gems", roleUnlocked = RoleId.HUNTER, isConsumable = false),
        ShopProduct("role_fool", "Unlock Fool", "Win if lynched", ProductType.ROLE_UNLOCK, "130 Gems", roleUnlocked = RoleId.FOOL, isConsumable = false),
        ShopProduct("role_arsonist", "Unlock Arsonist", "Douse & ignite", ProductType.ROLE_UNLOCK, "250 Gems", roleUnlocked = RoleId.ARSONIST, isConsumable = false),

        // Packs
        ShopProduct("roles_all_premium", "All Premium Roles", "Unlock every special role at once", ProductType.ROLE_PACK, "$7.99 / 800 Gems", isConsumable = false),
        ShopProduct("remove_ads", "Remove Ads Forever", "No more interstitial ads", ProductType.REMOVE_ADS, "$2.99 / 300 Gems", isConsumable = false),
        ShopProduct("starter_pack", "Starter Pack", "500 Gems + 20 Energy + 1 random premium role", ProductType.STARTER_PACK, "$1.99", gemsGranted = 500, energyGranted = 20, isConsumable = false),

        // Subscriptions
        ShopProduct("vip_monthly", "VIP Monthly", "No ads, +2 energy, exclusive roles, 100 gems/month", ProductType.VIP_SUBSCRIPTION, "$4.99/mo", isSubscription = true, isConsumable = false),
        ShopProduct("vip_yearly", "VIP Yearly", "Same as monthly + 2 months free", ProductType.VIP_SUBSCRIPTION, "$39.99/yr", isSubscription = true, isConsumable = false),
        ShopProduct("battle_pass", "Season Battle Pass", "Exclusive rewards track", ProductType.BATTLE_PASS, "$9.99", isConsumable = false)
    )

    fun getById(id: String) = products.find { it.id == id }
}