# IDT Table

[![CI](https://github.com/paulkugaev/IDT_Table/actions/workflows/ci.yml/badge.svg)](https://github.com/paulkugaev/IDT_Table/actions/workflows/ci.yml)

A Jetpack Compose Android app (test task) that builds a table of random data.
Two screens: enter the table size (rows/columns) and view/edit the table itself — a single click
highlights a cell (green), a double click edits its text.

## Features

- **Screen 1 — input:** rows (1–1000) and columns (1–6), with real-time validation.
- **Screen 2 — table:** a grid of random strings loaded from the data layer; single click toggles a
  green highlight, double click opens in-place text editing.

## Tech stack

- Kotlin 2.2, Jetpack Compose (Material 3)
- Gradle 9 (version catalog), AGP 9
- Clean Architecture + TEA (The Elm Architecture) on the UI
- Navigation: Modo
- DI: Hilt
- Pure layers: `domain` / `data` are plain Kotlin/JVM
- CI: GitHub Actions, detekt, pre-commit hook

## Architecture

```
        ┌────────────────────────────┐
        │            app             │   composition root:
        │  MainActivity · Hilt ·     │   Hilt wiring, Modo stack
        │  Modo stack                │
        └─────────────┬──────────────┘
                      │
        ┌─────────────▼──────────────┐   ┌────────────────────────────┐
        │        feature-*           │   │          common-*          │
        │  feature-input (api/impl)  │   │  common-ui   — theme/design │
        │  feature-table (api/impl)  │   │  common-tea   — TEA engine  │
        └─────────────┬──────────────┘   └────────────────────────────┘
                      │
        ┌─────────────▼──────────────┐   ┌────────────────────────────┐
        │           domain           │◄──│            data            │
        │  models · validator ·      │   │  DTO · mapper ·            │
        │  repository interface      │   │  repository implementation │
        └────────────────────────────┘   └────────────────────────────┘
```

- **domain** — plain Kotlin/JVM, depends on nothing (models, `TableSizeValidator`,
  `TableRepository`).
- **data** — implements the domain contracts (DTO, mapper, random data source).
- **feature-\*** — screens + TEA ViewModels, depend on domain.
- **common-\*** — shared infrastructure (theme, TEA engine).
- **app** — composition root: wires everything via Hilt, hosts the Modo stack.

Dependency arrows point inward — toward `domain` as the cleanest layer.

### TEA data flow (within a screen)

Each screen is a `StoreViewModel` (a plain `ViewModel`) hosting a pure `TeaStore`. The UI only
dispatches `UiEvent`s; side effects are described as `Command`s, run by `CommandsFlowHandler`s and
fed back into the reducer as `CommandResultEvent`s.

```
            UI
             │  dispatch(UiEvent)
             ▼
   ┌───────────────────────────┐
   │      StoreViewModel       │  Android host (extends ViewModel),
   │   (Store<State, ...>)     │  store launched in viewModelScope
   └────────────┬──────────────┘
                │  update(state, event)
                ▼
   ┌───────────────────────────┐
   │          TeaStore         │  pure reducer (Update):
   │   state{} · commands() ·  │  produces next State / Commands / News
   │   news()                  │
   └───────┬─────────────┬─────┘
           │ commands    │ news
           ▼             ▼
   ┌──────────────┐      one-shot events
   │   Command    │      (navigation, toast)
   │ side effect  │
   └──────┬───────┘
          │  CommandsFlowHandler (async)
          ▼
   CommandResultEvent ──────────► update() again
```

The reducer is a pure function — `state` and `news` are `StateFlow`/`Flow` exposed by the
`StoreViewModel`, so every screen is fully unit-testable without Android.

## Modules

`app` · `common-ui` · `common-tea` · `domain` · `data` · `feature-input-api/impl` ·
`feature-table-api/impl`

## Testing

Unit tests live in the pure/engine layers (`common-tea`, `domain`, `data`):

- **JUnit 4** — test runner and assertions
- **MockK** — mocking (`every` / `verify` / `capture`) for the repository tests
- **kotlinx-coroutines** — `runBlocking`, `runTest` and `Dispatchers.setMain` for `suspend` and
  coroutine-based tests

Covered today:

- **common-tea** — `TeaStore`: initial state, UiEvent→state transitions, UiEvent→news, command
  results fed back into the reducer, initial commands; `StoreViewModel`: state/news/dispatch and
  store lifecycle
- `TableSizeValidator` — boundary values, empty/out-of-range inputs, mixed per-axis errors
- `RandomStringDataSource` — length and alphabet contract
- `TableDataDtoMapper` — DTO → domain mapping
- `TableRepositoryImpl` — orchestration with mocked data source and mapper

## Build & run

```bash
./gradlew :app:assembleDebug   # build debug APK
./gradlew test                 # run unit tests
./gradlew detekt               # static analysis
```

Requires JDK 21 (Gradle daemon).

## Quality

- **detekt** — wired into every module, a gate in CI.
- **pre-commit hook** — `scripts/git-hooks/pre-commit` runs detekt on staged Kotlin/Gradle files.
  After cloning: `git config core.hooksPath scripts/git-hooks`.
- **GitHub Actions** — on PR: tests for affected modules only + detekt + build; on `main`: all
  tests.
