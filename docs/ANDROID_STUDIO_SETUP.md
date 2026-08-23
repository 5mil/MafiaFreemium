# Open Mafia Freemium in Android Studio

## Requirements
- Android Studio Ladybug (2024.2+) or newer
- JDK 17
- Android SDK 35

## Steps
1. Clone: https://github.com/5mil/MafiaFreemium
2. Android Studio → **Open** → select the `MafiaFreemium` folder (contains `settings.gradle.kts`)
3. Let Gradle sync
4. Install any missing SDK / build-tools if prompted
5. Run on emulator (API 26+) or a real device

## Gradle Wrapper
Android Studio generates the wrapper on first open if needed.
Or with Gradle installed:

```bash
cd MafiaFreemium
gradle wrapper --gradle-version 8.9
./gradlew assembleDebug
```

## Notes
- Completely **ad-free**
- Mock billing is enabled by default in `MainActivity` (`useMockBilling = true`)
- Switch to real `BillingManager` when you have Play Console products set up
- DataStore persists Diamonds, Hits, roles, and Made Man status
- Before publishing: see `docs/STORE_LAUNCH_CHECKLIST.md`
