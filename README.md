# DriveNext

DriveNext is a modern Android application for booking cars, built as part of the mobile development course. The app implements a clean multi-layer architecture (data/domain/presentation), edge-to-edge UI, theme switching, and an onboarding/auth flow. Data is kept locally with Jetpack DataStore until the Supabase backend is unlocked in later labs.

## Features

- Splash, onboarding, auth, and multi-step registration flows using Navigation Component + Safe Args
- Settings/profile screens with avatars plus theme and notification preferences persisted via DataStore
- Home/search experience with static car catalogue, loader state, error handling, and navigation to booking/detail placeholders
- Offline screen with connectivity monitoring (ConnectivityManager callbacks)
- Photo upload flow with camera/gallery support, runtime permissions, and persistent cache copies
- Full edge-to-edge layouts with consistent padding and status bar insets across screens
- Dark/light theme switching at runtime through `SessionRepository`
- CI pipeline (GitHub Actions) running lint, unit tests, and assembleDebug on pushes/PRs

## Project Structure

```
app/
  |- src/main/java/com/iwkms/drivenext
  |    |- data/          # DataStore + repositories
  |    |- domain/        # Models, repository interfaces
  |    |- presentation/  # Fragments, view models, adapters, util
  |- src/main/res        # Layouts, drawables, themes, values
```

## Requirements

- Android Studio Giraffe/Koala+ with AGP 8.x
- JDK 17 (aligned with CI)
- Android SDK 30-36 (minSdk 30, target 36)

## Getting Started

```bash
./gradlew lint testDebugUnitTest   # Lint + unit tests
./gradlew assembleDebug            # Build debug APK
./gradlew :app:installDebug        # (Optional) install on device/emulator
```

## Edge-to-Edge & Theming

Every root layout uses `@dimen/screen_padding` plus the `applyStatusBarPadding()` helper to respect cutouts while keeping consistent margins. Theme changes are propagated via `ThemeManager`, which listens to the `SessionRepository` DataStore flow and calls `AppCompatDelegate.setDefaultNightMode`.

## Continuous Integration

See [.github/workflows/ci.yml](.github/workflows/ci.yml). Each push/PR triggers lint, unit tests, and a debug build on `ubuntu-latest` with JDK 17.

## Localization

Base Russian strings live in `values/`, English translations in `values-en/`. Menu strings and all UI labels include translations to satisfy lint "not translated" checks.

## Future Work

- Replace static repositories with the real Supabase implementation once credentials become available
- Expand instrumentation tests (navigation, UI) under `app/src/androidTest`
- Hook up booking/detail screens to actual data sources

Feel free to open issues or PRs if you spot bugs or have suggestions.
