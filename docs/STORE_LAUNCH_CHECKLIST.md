# Play Store Launch Checklist — Mafia Freemium

## 1. Google Play Console
- [ ] Create app listing (name: **Mafia Freemium** or similar)
- [ ] Set package name: `com.mafiagame.freemium` (must match `applicationId`)
- [ ] Complete store listing (short/full description, screenshots, feature graphic, icon)
- [ ] Content rating questionnaire
- [ ] Target audience (typically 13+ / Teen for party game)
- [ ] Privacy policy URL
- [ ] Free app with in-app purchases

## 2. In-app products (exact IDs)
**Consumables:** gems_100, gems_500, gems_1200, gems_2500, gems_6500, gems_14000, energy_unlimited_24h  
**Non-consumables:** roles_all_premium, starter_pack, battle_pass  
**Subscriptions:** vip_monthly ($4.99/mo), vip_yearly ($39.99/yr)

See `docs/BILLING_SETUP.md` for details.

## 3. License testing
- [ ] Add license tester accounts
- [ ] Internal testing track
- [ ] Set `useMockBilling = false` for real billing tests
- [ ] Verify purchases grant Diamonds / Hits / roles / Made Man

## 4. Release build
- [ ] Create and secure upload keystore
- [ ] Build signed AAB
- [ ] Upload Internal testing → Production when ready

## 5. Policy
- [ ] No ads (by design)
- [ ] Clear IAP disclosure on listing
- [ ] Test API 26–35
- [ ] Verify DataStore after force-stop

## Design reminders
Pure microtransactions only. Core party game always free. No pay-to-win in deduction gameplay.
