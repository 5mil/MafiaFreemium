# Google Play Billing Setup — Mafia Freemium

## Dependency (build.gradle.kts)

```kotlin
dependencies {
    implementation("com.android.billingclient:billing-ktx:7.1.1")
}
```

## Product IDs to create in Google Play Console

### One-time products (Managed / In-app)
| Product ID              | Type        | Default Price |
|-------------------------|-------------|----------------|
| gems_100                | Consumable  | $0.99          |
| gems_500                | Consumable  | $4.99          |
| gems_1200               | Consumable  | $9.99          |
| gems_2500               | Consumable  | $19.99         |
| gems_6500               | Consumable  | $49.99         |
| gems_14000              | Consumable  | $99.99         |
| energy_unlimited_24h    | Consumable  | $1.99          |
| roles_all_premium       | Non-consumable | $7.99       |
| starter_pack            | Non-consumable | $1.99       |
| battle_pass             | Non-consumable | $9.99       |

Individual role unlocks and Hit packs that cost Diamonds are handled **inside the app** (spend Diamonds) and do **not** need Google Play products.

### Subscriptions
| Product ID     | Billing Period | Price   |
|----------------|----------------|---------|
| vip_monthly    | 1 month        | $4.99   |
| vip_yearly     | 1 year         | $39.99  |

## Flow
1. `BillingManager.startConnection()` on app start
2. Query product details → show real prices from Play in the Black Market
3. User taps a product → `launchPurchase(activity, productId)`
4. On success → `EconomyManager.applyPurchase(productId)` grants Diamonds / Hits / roles / Made Man status
5. Acknowledge the purchase

## Testing
- Use license tester accounts in Play Console
- Use `MockBillingManager` for UI previews and unit tests without a real device / Play Store

## Notes
- Completely ad-free — no AdMob or rewarded ads
- All monetization is pure microtransactions
- Consumables (Diamonds, temporary Hits) can be bought many times
- Non-consumables (Full Family Roster, Rookie Hitman Pack) and subscriptions are owned once
