# H&M Product Catalog - Android Application

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.12.00-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Min API](https://img.shields.io/badge/API-24%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A modern Android application showcasing H&M product catalog with infinite scroll pagination, built using Kotlin and Jetpack Compose.

## 📱 Features

- **Product Catalog**: Browse products in a responsive 2-column grid layout
- **Infinite Scroll**: Automatic pagination with load-more functionality
- **Error Handling**: Comprehensive error states with retry mechanisms
- **Accessibility**: Full screen reader support with semantic descriptions
- **Scroll to Top**: Quick navigation FAB after page threshold
- **Color Swatches**: Visual product color variants with overflow indicators
- **Material 3**: Modern UI with Material Design 3 components

## 🏗️ Architecture

This project follows **Clean Architecture** with clear separation of concerns:

```
app/
├── data/
│   ├── mapper/          # DTO to Domain model mapping
│   ├── remote/          # API services and DTOs
│   └── repository/      # Repository implementations
├── domain/
│   ├── model/           # Business models
│   └── repository/      # Repository interfaces
├── ui/
│   ├── screens/         # Composable screens and ViewModels
│   └── theme/           # Material 3 theming
├── di/                  # Dependency injection modules
└── util/                # Utility classes and extensions
```

### Architecture Layers

1. **Presentation Layer** (`ui/`)
   - Jetpack Compose UI
   - ViewModels with StateFlow and SharedFlow
   - UI State management

2. **Domain Layer** (`domain/`)
   - Domain models
   - Repository interfaces

3. **Data Layer** (`data/`)
   - Repository implementations
   - API services
   - Data mappers
   - Network error handling

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** 2.2.21 - Modern programming language
- **Jetpack Compose** - Declarative UI framework
- **Material 3** - Latest Material Design components
- **Coroutines** 1.10.2 - Asynchronous programming
- **StateFlow** - Reactive state management

### Dependency Injection
- **Dagger Hilt** 2.57.2 - Compile-time DI framework
- **KSP** 2.3.3 - Kotlin Symbol Processing

### Networking
- **Retrofit** 3.0.0 - Type-safe HTTP client
- **OkHttp** 5.3.2 - HTTP client with logging interceptor
- **Kotlinx Serialization** 1.9.0 - JSON serialization

### Image Loading
- **Coil** 2.7.0 - Image loading library for Compose

### Testing
- **JUnit** 4.13.2 - Unit testing framework
- **Mockito** 5.12.0 - Mocking framework
- **Turbine** 1.1.0 - Flow testing library
- **Compose UI Test** - UI testing framework

### Code Quality
- **Detekt** 1.23.8 - Static code analysis

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17 or higher
- Android SDK API 24+ (Android 7.0 Nougat)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/mustafizur-rahman26/HM-Android-code-test.git
```

2. Open the project in Android Studio

3. Build the project:
```bash
./gradlew build
```

4. Run on device or emulator:
```bash
./gradlew installDebug
```

### Configuration

The app uses H&M API. The base URL is configured in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"https://api.hm.com/\"")
```

## 📂 Project Structure

```
hmcodetest/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/hmcodetest/
│   │   │   │   ├── data/
│   │   │   │   │   ├── mapper/
│   │   │   │   │   ├── remote/
│   │   │   │   │   └── repository/
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   ├── pagination/
│   │   │   │   │   ├── repository/
│   │   │   │   │   └── usecase/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   └── theme/
│   │   │   │   ├── di/
│   │   │   │   └── util/
│   │   │   └── res/
│   │   ├── test/              # Unit tests
│   │   └── androidTest/       # Instrumented tests
│   └── build.gradle.kts
├── config/
│   └── detekt/               # Detekt configuration
├── gradle/
│   └── libs.versions.toml    # Version catalog
└── README.md
```

## 🎨 UI Components

### HomeScreen
- **ProductsContent**: Main composable displaying product grid
- **ProductGridItem**: Individual product card with image, brand, name, price, and swatches
- **ColorSwatchesRow**: Visual color variant indicators
- **Error States**: Empty state, initial error, and pagination error handling
- **Loading States**: Initial loading and load-more indicators
- **Scroll to Top FAB**: Appears after page 3

### Accessibility Features
- Semantic content descriptions for screen readers
- Live regions for dynamic content updates
- Minimum touch target sizes (48dp)

## 🔧 Key Implementation Details

### Pagination Strategy
- Custom pagination logic
- Automatic load-more when scrolling near bottom
- Prevents duplicate requests with state management
- Handles edge cases (no more pages, errors, already loading)

### Error Handling
- Comprehensive network error mapping
- User-friendly error messages
- Retry functionality for both initial and pagination errors
- Error state preservation during retry

### State Management
```kotlin
data class UiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 0,
    val nextPage: Int? = null
)
```

### Image Optimization
- Memory, disk, and network caching with Coil
- Placeholder and error states
- Optimized aspect ratio (0.75f) for product images

### Code Style
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Run Detekt before committing: `./gradlew detekt`
- Ensure all tests pass: `./gradlew test connectedAndroidTest`

## 📝 Code Quality

The project uses Detekt for static code analysis:

```bash
./gradlew detekt
```

Configuration: `config/detekt/detekt.yml`

---

**Made with ❤️ using Kotlin and Jetpack Compose**
