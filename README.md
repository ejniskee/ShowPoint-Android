# ShowPoint

Android app for tracking TV shows — browse series, rate them, and keep track of watched episodes.

## Features

- 📺 Browse a list of TV shows with posters, genres, and average ratings
- 📖 View show details — description, seasons, and episodes
- 🔐 User login & registration (JWT authentication)
- ⭐ Rate shows (1–5 stars) with optional comments
- ✅ Mark episodes as watched with progress percentage
- 🎥 Search for trailers on YouTube (implicit Intent)
- 📴 Offline access to the show list (Room cache)
- 📱 Adaptive UI — phone (single-pane) and tablet (master-detail)

## Architecture

The app follows **MVVM** with the **Repository pattern**:

```
View (Fragments) → ViewModel (LiveData) → Repository → Room / Retrofit
```

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java |
| UI | Fragments, RecyclerView, Material 3 |
| Architecture | MVVM, AndroidViewModel, LiveData |
| Networking | Retrofit 2, OkHttp, Gson |
| Local DB | Room (SQLite) |
| Images | Glide |
| Auth | JWT (SharedPreferences) |
| Backend | Spring Boot REST API (separate project) |

## Project Structure

```
app/src/main/java/com/example/showpointrma/
├── MainActivity.java              # Single Activity, fragment navigation
├── api/                           # Retrofit setup & API interface
├── data/
│   ├── local/                     # Room database (AppDatabase, DAO, Entity)
│   └── repository/                # SerijaRepository, DetaljiRepository, AuthRepository
├── model/                         # API models (Serija, Epizoda, Ocena, DTOs)
├── ui/                            # Fragments & RecyclerView adapters
├── util/                          # TokenManager (SharedPreferences)
└── viewmodel/                     # SerijeViewModel, SerijaDetaljiViewModel, AuthViewModel
```

## Getting Started

### Prerequisites

- Android Studio (Arctic Fox+)
- Min SDK: 24 (Android 7.0)
- A running [ShowPoint backend](.) on the local network

### Configuration

Set `BASE_URL` in `RetrofitClient.java` to your server address:

```java
// For emulator:
private static final String BASE_URL = "http://10.0.2.2:8080/ShowPoint/";

// For a physical device (your local IP):
private static final String BASE_URL = "http://192.168.x.x:8080/ShowPoint/";
```

### Build & Run

1. Open the project in Android Studio
2. Sync Gradle
3. Run on an emulator or device

## Notes

- The backend is a Spring Boot application and is not part of this repository
- `usesCleartextTraffic="true"` is enabled because the local server uses HTTP — use HTTPS in production
