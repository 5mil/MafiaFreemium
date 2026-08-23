# Mafia Freemium - Party Game Android App

**Our own freemium version inspired by classic Mafia/Werewolf party game**  
(inspired by: https://github.com/IamRezaMousavi/Mafia — original implementation, not a copy)

## Concept
Offline party-game narrator with pure microtransactions (**no ads**).

- Free core: Villager, Mafia, Doctor, Detective, night/day, voting
- Pay for more: Diamonds, Hits (energy), premium roles, Made Man status

## Tech
- Kotlin + Jetpack Compose + Material 3
- MVVM (`MafiaViewModel`)
- Google Play Billing
- DataStore persistence

## Open in Android Studio
See **docs/ANDROID_STUDIO_SETUP.md**

1. Clone https://github.com/5mil/MafiaFreemium
2. Open the folder in Android Studio
3. Gradle sync → Run (API 26+)

## Docs
- `docs/MICROTRANSACTIONS.md` — shop catalog
- `docs/BILLING_SETUP.md` — Play product IDs
- `docs/ANDROID_STUDIO_SETUP.md` — build instructions

## Status
- Core engine, economy, UI, billing, navigation, DataStore — done
- Night/day action UI + Settings + MainActivity — done
- Android Gradle project skeleton — done

## License
Original implementation. Inspired by classic Mafia gameplay only.
