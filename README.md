# Android Assessment

A Kotlin Android app demonstrating **Clean Architecture**, **Hilt DI**, **Room**, **Retrofit**, **Coroutines/Flow**, and a lifecycle-safe **polling mechanism** across three build flavors.

### Key Features
- **Screen 1 — Dog Breeds:** Expandable list, search, pull-to-refresh, shimmer loading, error retry, 35s polling
- **Screen 2 — Users:** List from JSONPlaceholder, click for details, Room caching, mock/prod data
- **Screen 3 — Settings:** Build variant, API endpoints, polling status, last fetch time, logging toggle

---

## Setup Instructions

### Prerequisites
| Tool | Version |
|---|---|
| Android Studio | Hedgehog (2023.1.1) or later |
| JDK | 17+ |
| Android SDK | compileSdk **36**, minSdk **24** |

### Clone & Open
```bash
git clone <repo-url>
cd AndroidAssessment
```
Open in Android Studio → *File → Open* → select the `AndroidAssessment` folder.  
Android Studio will sync Gradle automatically.

### Register the Application class
In `app/src/main/AndroidManifest.xml`, add:
```xml
<application
    android:name=".AppApplication"
    ...>
```

---

## Build Variants

The project has **one flavor dimension** (`environment`) with three flavors, combined with the standard `debug` / `release` build types, producing six variants:

| Variant | Flavor | `IS_LOGGING_ENABLED` | `USE_MOCK_DATA` | Minify |
|---|---|---|---|---|
| `devDebug` | dev | ✅ true | ✅ true | ❌ |
| `devRelease` | dev | ✅ true | ✅ true | ✅ |
| `mockDebug` | mock | ✅ true | ✅ true | ❌ |
| `mockRelease` | mock | ✅ true | ✅ true | ✅ |
| `prodDebug` | prod | ❌ false | ❌ false | ❌ |
| `prodRelease` | prod | ❌ false | ❌ false | ✅ |

### Build Commands

```bash
# Dev (mock data, logging on)
./gradlew assembleDevDebug

# Mock (explicit mock flavor)
./gradlew assembleMockDebug

# Prod release (minified, no logs, real API)
./gradlew assembleProdRelease

# Install directly to connected device
./gradlew installDevDebug
```

---

## Tech Stack

| Layer | Library |
|---|---|
| DI | Hilt 2.51.1 |
| Networking | Retrofit 2.11 + OkHttp 4.12 |
| Local DB | Room 2.6.1 |
| Async | Kotlin Coroutines 1.8 + Flow |
| Logging | Timber 5 (wrapped by `AppLogger`) |
| UI | Material 3, ViewBinding, Navigation Component 2.8 |
| Loading | Facebook Shimmer |

---

## Data Sources

| API | Base URL | Used in |
|---|---|---|
| Dog CEO | `https://dog.ceo/api/` | `DogApiService` (breeds, 35s polling) |
| JSONPlaceholder | `https://jsonplaceholder.typicode.com/` | `UsersApiService`, `PostsApiService` |
| Random User | `https://randomuser.me/` | `RandomUserApiService` |

---

## Project Structure

```
app/src/main/java/com/example/androidassessment/
├── data/
│   ├── local/          # Room entities, DAOs, DB, TypeConverter
│   ├── mapper/         # DTO/Entity ↔ Domain mappers
│   ├── mock/           # MockDogDataSource, MockUserDataSource, MockPostDataSource, MockRandomUserDataSource
│   ├── model/          # Network DTOs (BreedDto, UserResponse, PostResponse, RandomUserResponse)
│   ├── remote/         # RemoteDataSource, UserRemoteDataSource, API services
│   └── repository/     # DogRepositoryImpl, UserRepositoryImpl
├── di/                 # Hilt modules (App, Network, Database)
│   └── qualifier/      # @DogApi, @UsersApi, @RandomUserApi
├── domain/
│   ├── model/          # Breed, User (pure Kotlin)
│   ├── repository/     # DogRepository, UserRepository
│   └── usecase/        # GetBreedsUseCase, RefreshBreedsUseCase, GetUsersUseCase, RefreshUsersUseCase, GetUserByIdUseCase
├── presentation/
│   ├── breeds/         # BreedsFragment, BreedsViewModel, BreedAdapter
│   ├── users/          # UsersFragment, UserDetailFragment, UsersViewModel, UserDetailViewModel, UserAdapter
│   ├── settings/       # SettingsFragment, SettingsViewModel
│   └── common/         # BaseListAdapter
└── util/               # AppLogger, AppStateHolder
```

See [ARCHITECTURE.md](./ARCHITECTURE.md) for detailed architecture documentation.

---

## Architecture Overview

### Clean Architecture Layers

```text
┌─────────────────────────────────────────────┐
│              Presentation Layer             │
│  Fragments, ViewModels, Adapters, UI state  │
└────────────────────┬───────────────────────┘
                     │ uses
┌────────────────────▼───────────────────────┐
│                Domain Layer                │
│   Use cases, repository interfaces, models │
└────────────────────┬───────────────────────┘
                     │ implemented by
┌────────────────────▼───────────────────────┐
│                 Data Layer                 │
│  Repository impls, Room, Retrofit, mappers │
└────────────────────────────────────────────┘
```

- **Presentation:** `BreedsFragment`, `UsersFragment`, `SettingsFragment` and their ViewModels. They only talk to **use cases**, never directly to Room or Retrofit.
- **Domain:** Pure Kotlin; defines `DogRepository`, `UserRepository`, and use cases such as `GetBreedsUseCase`, `RefreshBreedsUseCase`, `GetUsersUseCase`.
- **Data:** Implements repositories using Room (`BreedDao`, `UserDao`), Retrofit services, and mock data sources. Domain models are produced via mappers.

### Data Flow (Breeds)

```text
UI (BreedsFragment)
   └─► BreedsViewModel
          ├─ observes GetBreedsUseCase()      (Flow<List<Breed>> from Room)
          └─ calls RefreshBreedsUseCase()     (on pull-to-refresh / polling)
                 └─► DogRepository.refreshBreeds()
                        ├─ if USE_MOCK_DATA → MockDogDataSource
                        └─ else            → RemoteDataSource → DogApiService
                        → Room (BreedDao.clearAll + insertAll)
                        → Flow emits new data back to ViewModel → UI
```

**Dependency direction:** Presentation → Domain → Data (never the other way).

---

## Build Variants Explanation

- **Flavor dimension:** `environment` with flavors `dev`, `mock`, `prod`.
- **Build types:** `debug` (no minify) and `release` (minify + shrink).

### Behavior Matrix

| Variant        | Flavor | Build type | `USE_MOCK_DATA` | `IS_LOGGING_ENABLED` | Minify / Shrink |
|----------------|--------|-----------|------------------|----------------------|-----------------|
| `devDebug`     | dev    | debug     | ✅ true          | ✅ true              | ❌              |
| `devRelease`   | dev    | release   | ✅ true          | ✅ true              | ✅              |
| `mockDebug`    | mock   | debug     | ✅ true          | ✅ true              | ❌              |
| `mockRelease`  | mock   | release   | ✅ true          | ✅ true              | ✅              |
| `prodDebug`    | prod   | debug     | ❌ false         | ❌ false             | ❌              |
| `prodRelease`  | prod   | release   | ❌ false         | ❌ false             | ✅              |

### How mock data is injected

- `app/build.gradle.kts` sets `BuildConfig.USE_MOCK_DATA` per flavor:
  - `dev`, `mock` → `USE_MOCK_DATA = true`
  - `prod`       → `USE_MOCK_DATA = false`
- Repositories read this flag:
  - `DogRepositoryImpl.refreshBreeds()`:
    - `true`  → `MockDogDataSource`
    - `false` → `RemoteDataSource` → `DogApiService`
  - `UserRepositoryImpl.refreshUsers()`:
    - `true`  → `MockUserDataSource`
    - `false` → `UserRemoteDataSource` → `UsersApiService`

### How logging is controlled

- `BuildConfig.IS_LOGGING_ENABLED`:
  - `dev` / `mock` → `true` → logging ON
  - `prod`        → `false` → logging OFF
- `AppApplication` plants Timber only when `IS_LOGGING_ENABLED` is true.
- `NetworkModule` adds `HttpLoggingInterceptor` only when `IS_LOGGING_ENABLED` is true.
- `AppLogger` checks `BuildConfig.IS_LOGGING_ENABLED` before delegating to Timber.
- `proguard-rules.pro` uses `-assumenosideeffects` on Timber so release builds strip logging calls.

---

## API Documentation

### Dog Breeds API (Primary)

- **Endpoint:** `GET https://dog.ceo/api/breeds/list/all`
- **Service:** `DogApiService.getAllBreeds()`
- **Response (simplified):**

```json
{
  "message": {
    "bulldog": ["boston", "english", "french"],
    "retriever": ["chesapeake", "golden"]
  },
  "status": "success"
}
```

- **Mapping:** `DogBreedsResponse` → `BreedEntity` → domain `Breed`.
- **Error handling:** exceptions are thrown from `RemoteDataSourceImpl` up to `BreedsViewModel.doRefresh()`, which catches and maps them to `BreedsUiState.error`.

### Users API

- **Endpoint:** `GET https://jsonplaceholder.typicode.com/users`
- **Service:** `UsersApiService.getUsers()`
- **Response:** array of user objects (id, name, username, email, address, company).
- **Mapping:** `UserResponse` → `UserEntity` → domain `User`.
- **Error handling:** errors bubble to `UserRepositoryImpl.refreshUsers()` and then to `UsersViewModel.refresh()`, which updates `UsersUiState.error`.

### Posts API (demo)

- **Endpoint:** `GET https://jsonplaceholder.typicode.com/posts`
- **Service:** `PostsApiService.getPosts()`
- Used as a demonstration of an additional Retrofit service.

### Random User API (demo)

- **Endpoint:** `GET https://randomuser.me/api/?results=10`
- **Service:** `RandomUserApiService.getUsers(results = 10)`
- Returns 10 random user objects; used to demonstrate a third-party API integration.

---

## Polling Mechanism

### Implementation Approach

- Implemented in `BreedsViewModel` using **Kotlin Coroutines** and `viewModelScope`:

```kotlin
private var pollingJob: Job? = null
private const val POLL_INTERVAL_MS = 35_000L // 35 seconds

fun startPolling() {
    if (pollingJob?.isActive == true) return
    pollingJob = viewModelScope.launch {
        while (isActive) {
            doRefresh()          // calls RefreshBreedsUseCase
            delay(POLL_INTERVAL_MS)
        }
    }
}

fun stopPolling() {
    pollingJob?.cancel()
    pollingJob = null
}
```

### Lifecycle Handling

- `BreedsViewModel` exposes a `DefaultLifecycleObserver` (`lifecycleObserver`).
- `BreedsFragment` registers it on `viewLifecycleOwner.lifecycle`:
  - `onStart` → `startPolling()`
  - `onStop`  → `stopPolling()`
- `onCleared()` in `BreedsViewModel` also calls `stopPolling()` as a safety net.

### Performance Considerations

- Polling runs **only when the Breeds screen is visible** (STARTED state), avoiding unnecessary work in the background.
- Uses a single coroutine in `viewModelScope`, so:
  - Automatically cancelled when the ViewModel is destroyed.
  - No busy-waiting; uses `delay(35_000L)` to sleep efficiently between refreshes.
- Room + Flow ensure only **changed data** triggers UI updates; no manual diffing in the ViewModel.

For a deeper dive into architecture, see `ARCHITECTURE.md`.
