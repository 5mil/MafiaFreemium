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

## Layout
```
MafiaFreemium/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── app/
│   ├── build.gradle.kts
│   └── src/main/ (Manifest + res)
└── src/main/kotlin/  (all app code)
```

The app module points Kotlin sources at `../src/main/kotlin`.

## Notes
- Ad-free, pure microtransactions
- Mock billing is on by default (`useMockBilling = true` in MainActivity)
- See `docs/BILLING_SETUP.md` for Play Console product IDs
