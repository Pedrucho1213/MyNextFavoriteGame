# MyNextFavoriteGame

Android app that lets users search for games on Google Play, view details, and save favorites — built as a technical assessment.

## Setup & Running

1. Clone the repository
2. Open with Android Studio Meerkat (or later)
3. The SerpAPI key is already included in `AppModule.kt` — no additional configuration needed
4. Run on an emulator or physical device with **API 26+**

> **Emulator DNS note:** if search returns no results on the emulator, go to Settings → Network → Private DNS → set to `dns.google`. This is a known emulator issue with some DNS servers.

## Architecture

**MVVM** with a clean separation of concerns across three layers:

```
presentation/   → Compose UI + ViewModels (no logic in @Composable functions)
data/           → Repositories, Room, Retrofit/Moshi DTOs and mappers
di/             → Hilt modules (AppModule, DatabaseModule, RepositoryModule)
navigation/     → Screen sealed class + NavHost wired in MainScreen
```

### Key decisions

**StateFlow over LiveData** — `StateFlow` integrates naturally with Kotlin coroutines and structured concurrency. All state is consumed with `collectAsStateWithLifecycle()` to respect the UI lifecycle and avoid unnecessary emissions when the app is in the background. `LiveData` was deliberately avoided.

**`debounce(600L) + distinctUntilChanged() + flatMapLatest`** — search queries are debounced before hitting the network. `flatMapLatest` cancels any in-flight request when a new query arrives, so only the most recent result is surfaced. `distinctUntilChanged` avoids redundant API calls for the same query.

**`SharingStarted.WhileSubscribed(5_000L)`** — the upstream flow stays active for 5 seconds after the last subscriber disconnects, surviving configuration changes (screen rotation) without re-fetching.

**Repository pattern** — `GameRepository` and `FavoritesRepository` are interfaces. `GameRepositoryImpl` handles network and returns `Result<T>`, keeping error handling out of the ViewModel. `FavoritesRepositoryImpl` wraps Room and exposes a `Flow<Set<String>>` of saved product IDs.

**Room for favorites** — favorites persist across sessions. The DAO exposes a `Flow<List<FavoriteEntity>>` that is mapped to a `Set<String>` and `combine`d with the search results in the ViewModel, so the heart icon state updates reactively without any additional calls.

**SerpAPI + Moshi** — the `google_play_games` engine returns `organic_results[].items[]` (a list of groups, each containing a list of games). A `OrganicResultGroup` wrapper DTO handles this structure. Results are flattened with `flatMap` in the repository before mapping to the domain model.

**Coil 3** — thumbnail URLs from the API use `=s64-rw` (64 px). The mapper replaces this with `=s512-rw` to load a higher-quality image for cards and the detail screen.

**Hilt 2.59.2 + KSP** — dependency injection throughout. `@HiltViewModel` for ViewModels, `@AndroidEntryPoint` on `MainActivity`, `@HiltAndroidApp` on the Application class.

**Material 3 / Material You** — dynamic color is enabled via `dynamicColorScheme` on Android 12+, falling back to a static color scheme on older versions.

**`AnimatedVisibility`** — the clear (×) button in the search field uses `AnimatedVisibility` with a fade + scale transition, covering the bonus animation requirement.

## Tech stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.9.7 |
| DI | Hilt 2.59.2 |
| Network | Retrofit 2.11.0 + OkHttp 4.12.0 + Moshi 1.15.2 |
| Images | Coil 3.4.0 |
| Local storage | Room 2.7.1 |
| Async | Kotlin Coroutines + Flow |
| Testing | Compose UI Test (`createComposeRule`) |

## Tests

Four instrumented Compose UI tests in `androidTest/`:

- `FavoritesScreenTest` — empty state shows "No favorites yet" / with games shows the game title
- `HomeScreenTest` — error state shows a "Retry" button / clicking Retry invokes the callback

Run with a connected device or emulator:
```bash
./gradlew connectedDebugAndroidTest
```

## Known trade-offs

**Favorites are filtered from in-memory search results** — Room persists only the `productId` of each favorite. The full `Game` object (title, thumbnail, description, etc.) comes from the API response held in `HomeViewModel`. This means the Favorites screen only shows games that are present in the current search results. A production implementation would either store the full game object in Room or expose a dedicated endpoint to fetch game details by ID.

**Pagination** — the SerpAPI response does not include standard pagination tokens in the `google_play_games` engine. Implementing infinite scroll would require chaining requests by offset or switching to a different engine. Given time constraints this was left as a trade-off in favor of completing the full feature set.

**`Game` domain model location** — the `Game` data class lives in `presentation/components/` rather than a dedicated `domain/model/` package. In a full clean-architecture setup it would sit in a separate `domain` module with no dependency on either `data` or `presentation`. Moving it was deferred due to time.

**ViewModel unit tests** — the `HomeViewModel` logic (debounce, flatMapLatest, combine) is best tested with `kotlinx-coroutines-test` and `Turbine`. These tests were omitted due to time, but the instrumented Compose tests cover the UI layer end-to-end.

**Detail screen data source** — the detail screen receives the `Game` object via the in-memory state from `HomeViewModel`. A production implementation would fetch the detail from a dedicated API endpoint or cache it in Room.
