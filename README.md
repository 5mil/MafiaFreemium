# Mafia Freemium - Party Game Android App

**Our own freemium version inspired by classic Mafia/Werewolf party game** (original open-source: https://github.com/IamRezaMousavi/Mafia)

## Concept
Local multiplayer narrator app for social deduction party game, enhanced with freemium monetization via pure microtransactions (no ads).

**Core free-to-play**:
- Basic roles: Villager, Mafia, Doctor, Detective
- Standard night/day phases, voting, role assignment
- Multi-language support (EN, FA, ES, more)
- Offline play with one device as narrator

**Freemium monetization (ad-free)**:
- Soft currency (Cash) earned by playing
- Hard currency (Diamonds) bought with real money
- Hits system (energy) limiting free games per day
- Unlockable premium roles, cosmetics, themes, voice packs
- Made Man subscription, Family Legacy Pass
- Pure microtransactions only — more content + reduce time

## Tech Stack (proposed)
- Kotlin + Jetpack Compose (modern UI)
- Clean Architecture + MVVM
- Google Play Billing Library v7+ for IAPs
- Room / DataStore for local persistence
- Material 3 Design with underworld theme

## Microtransactions Catalog
See docs/MICROTRANSACTIONS.md

## Billing Setup
See docs/BILLING_SETUP.md

## Game Mechanics
Same classic Mafia:
- Secret role assignment
- Night phase (kills, protect, investigate)
- Day phase (discussion + vote)
- Win conditions for Village / Mafia / Independents

Premium roles add complexity and fun (buy to unlock).

## License Note
This is a **from-scratch original implementation**. We do **not** copy GPL-3.0 code from the original repo. Inspired only by the game concept and UI flow.

## Status (2026-08-22)
- Core game engine (roles, night/day, voting) — done
- Full freemium economy (Diamonds, Hits, Shop, Made Man) — done & self-tested
- Completely **ad-free** (pure microtransactions only)
- Jetpack Compose UI screens (Home, Black Market, Setup, Role Reveal, Night/Day) — done
- Google Play Billing integration (BillingManager + Mock) — done
- GitHub: https://github.com/5mil/MafiaFreemium
- Next: Navigation host + ViewModel wiring, then persistence
