# Mafia Freemium - Party Game Android App

**Our own freemium version inspired by classic Mafia/Werewolf party game** (original open-source: https://github.com/IamRezaMousavi/Mafia)

## Concept
Local multiplayer narrator app for social deduction party game, enhanced with freemium monetization via lots of microtransactions.

**Core free-to-play**:
- Basic roles: Villager, Mafia, Doctor, Detective
- Standard night/day phases, voting, role assignment
- Multi-language support (EN, FA, ES, more)
- Offline play with one device as narrator

**Freemium monetization**:
- Soft currency (Coins) earned by playing
- Hard currency (Gems) bought with real money
- Energy system limiting free games per day
- Unlockable premium roles, cosmetics, themes, voice packs
- Remove ads, VIP subscription, battle pass
- Many individual microtransactions

## Tech Stack (proposed)
- Kotlin + Jetpack Compose (modern UI)
- Clean Architecture + MVVM
- Google Play Billing Library v7+ for IAPs
- Room / DataStore for local persistence
- Optional: Firebase for online rooms / leaderboards (future)
- AdMob for free-tier ads
- Material 3 Design

## Microtransactions Catalog (lots!)
See docs/MICROTRANSACTIONS.md

## Game Mechanics
Same classic Mafia:
- Secret role assignment
- Night phase (kills, protect, investigate)
- Day phase (discussion + vote)
- Win conditions for Village / Mafia / Independents

Premium roles add complexity and fun (buy to unlock).

## License Note
This is a **from-scratch original implementation**. We do **not** copy GPL-3.0 code from the original repo. Inspired only by the game concept and UI flow.

## Current Status (2026-08-22)
- Core game engine (roles, night/day, voting) — done
- Full freemium economy (Energy, Gems, Coins, ShopCatalog, EconomyManager) — done & self-tested
- GitHub repo live: https://github.com/5mil/MafiaFreemium
- Next up: Jetpack Compose UI screens + Google Play Billing integration
