package com.mafiagame.freemium.model

/**
 * Core freemium economy models — fully Mafia-themed.
 * Soft currency = Cash (earned by finishing jobs)
 * Hard currency = Diamonds (bought with real money)
 * Energy = Hits (how many jobs/games you can run today)
 *
 * Completely ad-free. Monetization is pure microtransactions only.
 */

data class PlayerWallet(
    var coins: Int = 0,                  // Cash
    var gems: Int = 0,                   // Diamonds
    var energy: Int = 5,                 // Hits remaining
    var maxEnergy: Int = 5,              // free crews
    var lastEnergyRefresh: Long = 0L,    // epoch millis for daily reset
    var isVip: Boolean = false,          // Made Man status
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
        // Diamond packs
        ShopProduct("gems_100", "100 Diamonds", "Small envelope of ice", ProductType.GEM_PACK, "$0.99", gemsGranted = 100),
        ShopProduct("gems_500", "550 Diamonds", "Capo’s cut (+10% bonus)", ProductType.GEM_PACK, "$4.99", gemsGranted = 550),
        ShopProduct("gems_1200", "1400 Diamonds", "Underboss share (+16%)", ProductType.GEM_PACK, "$9.99", gemsGranted = 1400),
        ShopProduct("gems_2500", "3000 Diamonds", "Family vault (+20%)", ProductType.GEM_PACK, "$19.99", gemsGranted = 3000),
        ShopProduct("gems_6500", "8000 Diamonds", "Godfather’s reserve (+23%)", ProductType.GEM_PACK, "$49.99", gemsGranted = 8000),
        ShopProduct("gems_14000", "18000 Diamonds", "Empire treasury (+28%)", ProductType.GEM_PACK, "$99.99", gemsGranted = 18000),

        // Hits (Energy) — main time-gate reduction
        ShopProduct("energy_5", "5 Hits", "Quick reload for the crew", ProductType.ENERGY_PACK, "50 Diamonds", energyGranted = 5),
        ShopProduct("energy_20", "20 Hits", "Full crew restock", ProductType.ENERGY_PACK, "150 Diamonds", energyGranted = 20),
        ShopProduct("energy_50", "50 Hits", "Arsenal ready", ProductType.ENERGY_PACK, "300 Diamonds", energyGranted = 50),
        ShopProduct("energy_unlimited_24h", "All-Night Operation", "Unlimited Hits for 24 hours", ProductType.ENERGY_PACK, "$1.99 / 200 Diamonds", energyGranted = 999),

        // Individual premium roles
        ShopProduct("role_bodyguard", "Recruit Bodyguard", "Your personal muscle — dies for you and hits back", ProductType.ROLE_UNLOCK, "150 Diamonds", roleUnlocked = RoleId.BODYGUARD, isConsumable = false),
        ShopProduct("role_jester", "Recruit Jester", "The clown who wins by getting executed", ProductType.ROLE_UNLOCK, "120 Diamonds", roleUnlocked = RoleId.JESTER, isConsumable = false),
        ShopProduct("role_serial_killer", "Recruit Serial Killer", "Lone wolf who answers to no family", ProductType.ROLE_UNLOCK, "200 Diamonds", roleUnlocked = RoleId.SERIAL_KILLER, isConsumable = false),
        ShopProduct("role_mayor", "Recruit Mayor", "Double voting power at the town meeting", ProductType.ROLE_UNLOCK, "100 Diamonds", roleUnlocked = RoleId.MAYOR, isConsumable = false),
        ShopProduct("role_vigilante", "Recruit Vigilante", "Takes justice into his own hands during the day", ProductType.ROLE_UNLOCK, "180 Diamonds", roleUnlocked = RoleId.VIGILANTE, isConsumable = false),
        ShopProduct("role_witch", "Recruit Witch", "One poison vial, one antidote", ProductType.ROLE_UNLOCK, "220 Diamonds", roleUnlocked = RoleId.WITCH, isConsumable = false),
        ShopProduct("role_cupid", "Recruit Cupid", "Links two souls — they live or die together", ProductType.ROLE_UNLOCK, "160 Diamonds", roleUnlocked = RoleId.CUPID, isConsumable = false),
        ShopProduct("role_hunter", "Recruit Hunter", "When you go down, you take one with you", ProductType.ROLE_UNLOCK, "140 Diamonds", roleUnlocked = RoleId.HUNTER, isConsumable = false),
        ShopProduct("role_fool", "Recruit Fool", "Wins only if the town hangs you", ProductType.ROLE_UNLOCK, "130 Diamonds", roleUnlocked = RoleId.FOOL, isConsumable = false),
        ShopProduct("role_arsonist", "Recruit Arsonist", "Douse the city… then light the match", ProductType.ROLE_UNLOCK, "250 Diamonds", roleUnlocked = RoleId.ARSONIST, isConsumable = false),

        // Packs & status
        ShopProduct("roles_all_premium", "Full Family Roster", "Unlock every special role at once", ProductType.ROLE_PACK, "$7.99 / 800 Diamonds", isConsumable = false),
        ShopProduct("starter_pack", "Rookie Hitman Pack", "500 Diamonds + 20 Hits + 1 random premium role", ProductType.STARTER_PACK, "$1.99", gemsGranted = 500, energyGranted = 20, isConsumable = false),

        // Subscriptions
        ShopProduct("vip_monthly", "Made Man Monthly", "+2 daily Hits • exclusive roles • exclusive cosmetics • 100 Diamonds/month", ProductType.VIP_SUBSCRIPTION, "$4.99/mo", isSubscription = true, isConsumable = false),
        ShopProduct("vip_yearly", "Made Man Yearly", "Same as monthly + 2 months free + exclusive Don badge", ProductType.VIP_SUBSCRIPTION, "$39.99/yr", isSubscription = true, isConsumable = false),
        ShopProduct("battle_pass", "Family Legacy Pass", "Season-long exclusive rewards for the family", ProductType.BATTLE_PASS, "$9.99", isConsumable = false)
    )

    fun getById(id: String) = products.find { it.id == id }
}