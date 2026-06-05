# 🏠 LifeHub

> Your life, organized. A modern Android app that combines Todo, Notes, Budget Tracker, and Habit Tracker into one clean hub.

## ✨ Features

- ✅ **Todo List** — Create, complete, and manage tasks with priorities
- 📝 **Notes** — Color-coded notes with search functionality
- 💰 **Budget Tracker** — Track income & expenses with categories
- 🎯 **Habit Tracker** — Build habits with streak tracking

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| Database | Room (SQLite) |
| DI | Hilt |
| Navigation | Navigation Compose |
| Async | Coroutines + Flow |

## 📂 Architecture

```
com.fizi.lifehub/
├── data/          ← Data layer (Room entities, DAOs, repositories)
├── domain/        ← Domain layer (models, repository interfaces, use cases)
├── di/            ← Hilt dependency injection modules
└── ui/            ← UI layer (Compose screens, ViewModels, theme)
    ├── theme/     ← Material 3 theme (colors, typography)
    ├── navigation/← NavHost + bottom navigation
    ├── todo/      ← Todo feature
    ├── notes/     ← Notes feature
    ├── budget/    ← Budget tracker feature
    └── habits/    ← Habit tracker feature
```

## 🚀 How to Build

1. Open the project in **Android Studio Hedgehog** (or newer)
2. Sync Gradle (it will download all dependencies)
3. Run on emulator or physical device (API 26+)

## 📱 Screenshots

*Coming soon — build and try it yourself!*

## 📄 License

Open source — built with ❤️ by Fizi

---

> *"Organize your life, one hub at a time."*
