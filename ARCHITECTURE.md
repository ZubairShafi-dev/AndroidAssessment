# Architecture

This document explains the app’s architecture in depth: **layer responsibilities**, **data flow**, **dependency injection**, **error handling**, and **state management**.

---

## 1. Clean Architecture Overview

The project follows a classic **Clean Architecture** split into three layers, with **dependencies pointing inward only**:

```text
┌─────────────────────────────────────────────┐
│              Presentation Layer             │
│  Fragments, ViewModels, UI state, Adapters │
└────────────────────┬───────────────────────┘
                     │ uses
┌────────────────────▼───────────────────────┐
│                Domain Layer                │
│   Use cases, repository interfaces, models │
└────────────────────┬───────────────────────┘
                     │ implemented by
┌────────────────────▼───────────────────────┐
│                 Data Layer                 │
│ Repositories, Room, Retrofit, mock sources │
└────────────────────────────────────────────┘
```

- **Presentation** depends on **Domain**.
- **Domain** depends on **nothing** (pure Kotlin).
- **Data** depends on **Domain** (implements the interfaces).

---

## 2. Layer Responsibilities

### 2.1 Presentation Layer

**Location:** `presentation/`

**Responsibilities:**

- Display UI and handle user interactions.
- Collect `StateFlow` from ViewModels and render based on immutable UI state objects.
- Delegate user actions (refresh, retry, navigation) to ViewModels.

**Key components:**

- `BreedsFragment`, `BreedsViewModel`, `BreedAdapter`
- `UsersFragment`, `UserDetailFragment`, `UsersViewModel`, `UserDetailViewModel`, `UserAdapter`
- `SettingsFragment`, `SettingsViewModel`

**Rules:**

- No direct calls to Room or Retrofit.
- Only talk to the domain layer via **use cases**.

---

### 2.2 Domain Layer

**Location:** `domain/`

**Responsibilities:**

- Define **business contracts** and **use cases**.
- Contain **pure Kotlin** code: no Android or framework dependencies.
- Be easily testable.

**Key components:**

- Domain models: `Breed`, `User`
- Repository interfaces:
  - `DogRepository`
  - `UserRepository`
- Use cases:
  - `GetBreedsUseCase`
  - `RefreshBreedsUseCase`
  - `GetUsersUseCase`
  - `RefreshUsersUseCase`
  - `GetUserByIdUseCase`

**Rules:**

- No awareness of Retrofit, Room, or Hilt.
- Knows only about repository interfaces, not their implementations.

---

### 2.3 Data Layer

**Location:** `data/`

**Responsibilities:**

- Implement repository interfaces defined in the domain layer.
- Manage all data sources:
  - Remote APIs (Retrofit)
  - Local cache (Room)
  - Mock data (for dev/mock flavors)
- Map DTOs and entities to domain models.

**Key components:**

- Repositories:
  - `DogRepositoryImpl`
  - `UserRepositoryImpl`
- Remote:
  - `RemoteDataSource`, `RemoteDataSourceImpl`
  - `UserRemoteDataSource`, `UserRemoteDataSourceImpl`
  - Retrofit services: `DogApiService`, `UsersApiService`, `PostsApiService`, `RandomUserApiService`
- Local:
  - `AppDatabase`
  - DAOs: `BreedDao`, `UserDao`
  - Entities: `BreedEntity`, `UserEntity`
  - Converter: `StringListConverter` for `List<String>` sub-breeds
- Mock:
  - `MockDogDataSource`
  - `MockUserDataSource`
  - `MockPostDataSource`
  - `MockRandomUserDataSource`
- Mappers:
  - `BreedMapper`
  - `UserMapper`

**Rules:**

- Does not expose Room entities or Retrofit DTOs to the domain or presentation – always map to domain models.
- Chooses between **mock** and **remote** based on `BuildConfig.USE_MOCK_DATA`.

---

## 3. Data Flow

### 3.1 Breeds (Primary Feature)

**High-level flow:**

```text
BreedsFragment
  └─► BreedsViewModel
        ├─ observes GetBreedsUseCase()           // Flow<List<Breed>> from Room
        └─ triggers RefreshBreedsUseCase()       // manual refresh or polling
                └─► DogRepository.refreshBreeds()
                        ├─ if USE_MOCK_DATA → MockDogDataSource
                        └─ else            → RemoteDataSource.getBreeds()
                                                └─ DogApiService.getAllBreeds()
                        └─► BreedDao.clearAll() + insertAll()
                                └─► BreedDao.observeAllBreeds()
                                        └─► GetBreedsUseCase() Flow emits
                                                └─► BreedsViewModel.uiState
                                                        └─► BreedsFragment UI
```

### 3.2 Users (Secondary Feature)

```text
UsersFragment
  └─► UsersViewModel
        ├─ observes GetUsersUseCase()            // Flow<List<User>> from Room
        └─ triggers RefreshUsersUseCase()
                └─► UserRepository.refreshUsers()
                        ├─ if USE_MOCK_DATA → MockUserDataSource
                        └─ else            → UserRemoteDataSource.getUsers()
                                                └─ UsersApiService.getUsers()
                        └─► UserDao.clearAll() + insertAll()
                                └─► UserDao.observeAllUsers()
                                        └─► GetUsersUseCase() Flow emits
                                                └─► UsersViewModel.uiState
                                                        └─► UsersFragment UI
```

### 3.3 State Sharing Between Screens

`AppStateHolder` is a Hilt `@Singleton` that shares cross-screen state:

- `isPollingActive: StateFlow<Boolean>`
- `lastFetchTime: StateFlow<Instant?>`

`BreedsViewModel` writes:

- `setPollingActive(active: Boolean)`
- `setLastFetchTime(time: Instant)`

`SettingsViewModel` reads these flows to show **Polling Active** and **Last Fetch** in the Settings screen.

---

## 4. Dependency Injection Strategy

**Library:** Hilt

### 4.1 Application-level setup

- `AppApplication` is annotated with `@HiltAndroidApp`.
- All activities/fragments that need injection are annotated with `@AndroidEntryPoint`.
- ViewModels use `@HiltViewModel` and are constructed via Hilt.

### 4.2 Modules

**`AppModule`** (`SingletonComponent`):

- Binds repository interfaces to implementations:

```kotlin
@Binds
@Singleton
abstract fun bindDogRepository(impl: DogRepositoryImpl): DogRepository

@Binds
@Singleton
abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
```

- Binds remote data sources:

```kotlin
@Binds
@Singleton
abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

@Binds
@Singleton
abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource
```

**`DatabaseModule`**:

- Provides `AppDatabase`, `BreedDao`, `UserDao` as singletons.

**`NetworkModule`**:

- Provides a shared `OkHttpClient`, with `HttpLoggingInterceptor` enabled only when `BuildConfig.IS_LOGGING_ENABLED` is true.
- Provides Retrofit instances for:
  - `@DogApi` (`https://dog.ceo/api/`)
  - `@UsersApi` (`https://jsonplaceholder.typicode.com/`)
  - `@RandomUserApi` (`https://randomuser.me/`)
- Provides API services:
  - `DogApiService`
  - `UsersApiService`
  - `PostsApiService`
  - `RandomUserApiService`

**Qualifiers:** `@DogApi`, `@UsersApi`, `@RandomUserApi` distinguish Retrofit instances with different base URLs.

---

## 5. Error Handling Patterns

### 5.1 Repositories

- Repositories wrap network calls and **re-throw** exceptions to be handled by ViewModels, rather than swallowing them.

Example (`DogRepositoryImpl.refreshBreeds`):

```kotlin
override suspend fun refreshBreeds() {
    val entities = if (BuildConfig.USE_MOCK_DATA) {
        mockDataSource.getBreedEntities()
    } else {
        try {
            val dtos = remoteDataSource.getBreeds()
            dtos.map { ... }
        } catch (e: Exception) {
            AppLogger.e(tag, "Failed to fetch breeds from remote", e)
            throw e
        }
    }
    breedDao.clearAll()
    breedDao.insertAll(entities)
}
```

### 5.2 ViewModels

ViewModels translate exceptions into UI-friendly error messages stored in `UiState.error`.

Example (`BreedsViewModel.doRefresh`):

```kotlin
private suspend fun doRefresh() {
    _uiState.update { it.copy(isLoading = true, error = null) }
    try {
        refreshBreeds()
        _uiState.update { it.copy(isLoading = false, lastUpdated = Instant.now(), error = null) }
    } catch (e: CancellationException) {
        _uiState.update { it.copy(isLoading = false) }
        throw e
    } catch (e: Exception) {
        val msg = e.message ?: "Unknown error"
        _uiState.update { it.copy(isLoading = false, error = msg) }
    }
}
```

Fragments then show an **error layout** with a retry button when `error != null` and no data is present.

---

## 6. State Management

### 6.1 UI State Data Classes

Each screen has a single, immutable `UiState` data class:

- `BreedsUiState`
  - `breeds: List<Breed>`
  - `isLoading: Boolean`
  - `error: String?`
  - `lastUpdated: Instant?`
  - `isPollingActive: Boolean`

- `UsersUiState`
  - `users: List<User>`
  - `isLoading: Boolean`
  - `error: String?`

- `SettingsUiState` (conceptual, though fields are surfaced directly in `SettingsViewModel`)

`MutableStateFlow<UiState>` is held inside the ViewModel and exposed as `StateFlow<UiState>` to the Fragment.

### 6.2 Collection in Fragments

Fragments use `repeatOnLifecycle(Lifecycle.State.STARTED)` to safely collect `StateFlow` and avoid leaks:

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state ->
            renderState(state)
        }
    }
}
```

### 6.3 Shared State

`AppStateHolder` shares polling status and last fetch time between:

- Writer: `BreedsViewModel`
- Reader: `SettingsViewModel`

Using `MutableStateFlow` → `StateFlow`.

---

## 7. Polling Mechanism (Detail)

### 7.1 Requirements

- Fetch dogs every **35 seconds**.
- Only when app/screen is in the foreground.
- Stop when screen goes to background.
- Resume when returning to foreground.

### 7.2 Implementation

Implemented in `BreedsViewModel` with a `Job` and `viewModelScope`:

```kotlin
private var pollingJob: Job? = null
private const val POLL_INTERVAL_MS = 35_000L

fun startPolling() {
    if (pollingJob?.isActive == true) return
    pollingJob = viewModelScope.launch {
        _uiState.update { it.copy(isPollingActive = true) }
        appStateHolder.setPollingActive(true)

        while (isActive) {
            doRefresh()
            delay(POLL_INTERVAL_MS)
        }
    }
}

fun stopPolling() {
    pollingJob?.cancel()
    pollingJob = null
    _uiState.update { it.copy(isPollingActive = false) }
    appStateHolder.setPollingActive(false)
}
```

Lifecycle integration:

```kotlin
val lifecycleObserver: DefaultLifecycleObserver = object : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) = startPolling()
    override fun onStop(owner: LifecycleOwner) = stopPolling()
}
```

Attached in `BreedsFragment`:

```kotlin
viewLifecycleOwner.lifecycle.addObserver(viewModel.lifecycleObserver)
```

### 7.3 Performance Considerations

- The polling loop is **suspended** most of the time (via `delay`), so it does not busy-wait.
- Only runs while the Breeds screen is at least STARTED.
- Uses a single coroutine tied to `viewModelScope`, so it is automatically cancelled when the ViewModel is cleared.

---

This architecture aims to keep each layer focused, make testing easier, and satisfy the assessment requirements: Clean Architecture, proper build variants, API integration, periodic polling, and robust logging and error handling patterns.

