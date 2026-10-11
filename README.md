# GlassKit

GlassKit is a Jetpack Compose library for liquid-glass surfaces, interactive glass controls, and backdrop blur on Android.

![Android API 21+](https://img.shields.io/badge/API-21%2B-brightgreen.svg)
![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)

---

## Features

- **`GlassContainer`**: Captures background layers and provides shared backdrop state for nested glass elements.
- **`GlassCard`**: Frosted glass container for cards and panels.
- **`GlassSurface`**: Adjustable backdrop blur surface with dynamic corner radiuses and surface tinting.
- **`GlassButton`**: Interactive frosted glass button with tactile touch feedback.
- **`GlassBlur`**: Custom clipped, adjustable backdrop blur modifier.
- **`GlassTheme`**: Fully customizable global or local theme configuring colors, blur ratios, corner shapes, and refraction effects.
- **Android Compatibility**: Android 12+ (API 31+) hardware-accelerated blur; older API levels safely fall back to unblurred translucent layouts.

---

## Installation

### Option 1: Maven Central

Ensure `mavenCentral()` is included in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

Add the dependency to your app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.ajayvamsee:glasskit:1.0.0")
}
```

### Option 2: JitPack (GitHub Releases)

Add the JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency using the GitHub repository path:

```kotlin
dependencies {
    implementation("com.github.ajayvamsee:GlassKit:1.0.0")
}
```

### Option 3: Local Publication (`mavenLocal`)

If you want to test local builds without remote publishing:

1. Build and publish to your local Maven repository:
   ```shell
   ./gradlew :glasskit:publishToMavenLocal
   ```
2. Add `mavenLocal()` to your project's `settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           mavenLocal()
           google()
           mavenCentral()
       }
   }
   ```
3. Include the dependency in `build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation("io.github.ajayvamsee:glasskit:1.0.0")
   }
   ```

---

## Quick Start

Wrap your UI hierarchy in `GlassContainer` and pass your background view (such as gradients, photos, or canvas drawings) into the `background` slot. Any descendant `GlassCard`, `GlassSurface`, or `GlassButton` automatically samples and blurs the underlying content.

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.ajayvamsee.glasskit.components.GlassButton
import io.github.ajayvamsee.glasskit.components.GlassCard
import io.github.ajayvamsee.glasskit.components.GlassContainer
import io.github.ajayvamsee.glasskit.components.GlassSurface
import io.github.ajayvamsee.glasskit.theme.GlassTheme

@Composable
fun GlassExample() {
    GlassTheme(darkTheme = true) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF3B0764), Color(0xFF0284C7))
                            )
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Frosted Glass Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Frosted Glass Card", color = Color.White)
                    }
                }

                // Custom Blur Surface
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    blurRadius = 25.dp
                ) {
                    Text("Adjustable Blur Surface (25dp)", Modifier.padding(20.dp), color = Color.White)
                }

                // Interactive Glass Button
                GlassButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
```

---

## Customization Guide & Parameter Types

Developers integrating GlassKit can fully customize the library's appearance—including palette colors, default blur ratios, corner shapes, and 3D refraction lens dispersion—globally via `GlassTheme` or per component.

> 💡 **Sample Code**: Runnable customization code examples are available in [CustomGlassExamples.kt](file:///C:/Users/e1775510.TILGE/AndroidStudioProjects/GlassKit/app/src/main/java/io/github/ajayvamsee/glasskit/samples/CustomGlassExamples.kt).

### Customizable Parameter Types

| Configuration Domain | Property | Type | Description |
| :--- | :--- | :--- | :--- |
| **`GlassColors`** | `surfaceTint` | `Color` | Translucent color tint drawn over frosted glass surfaces |
| | `backgroundOverlay` | `Color` | Color overlay for glass containers |
| | `accent` | `Color` | Accent color for buttons, active sliders, and toggles |
| | `borderColor` | `Color` | Edge highlight and glass outline accent color |
| **`GlassShapes`** | `cardShape` | `Shape` | Shape for `GlassCard` (e.g. `RoundedCornerShape`, `Capsule`) |
| | `surfaceShape` | `Shape` | Shape for `GlassSurface` |
| | `buttonShape` | `Shape` | Shape for `GlassButton` & `LiquidButton` |
| | `cornerRadius` | `Dp` | Default corner radius dimension (e.g., `24.dp`) |
| **`GlassRefraction`** | `depth` | `Dp` | Refraction lens edge height/depth dimension |
| | `chromaticAberration` | `Boolean` | Enables/disables RGB color dispersion on refracted glass edges |
| | `highlightAlpha` | `Float` | Alpha opacity (0.0f to 1.0f) for specular light highlights |
| **Blur Settings** | `blurRadius` | `Dp` | Default backdrop blur radius in Dp (e.g., `16.dp`, `24.dp`) |

---

### Customization Usage Examples

#### 1. Global Theme Customization

Pass custom `colors`, `blurRadius`, `shapes`, and `refraction` directly into `GlassTheme`:

```kotlin
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.ajayvamsee.glasskit.theme.GlassColors
import io.github.ajayvamsee.glasskit.theme.GlassRefraction
import io.github.ajayvamsee.glasskit.theme.GlassShapes
import io.github.ajayvamsee.glasskit.theme.GlassTheme

@Composable
fun CustomAppTheme(content: @Composable () -> Unit) {
    GlassTheme(
        darkTheme = true,
        // 1. Customize Colors
        colors = GlassColors(
            surfaceTint = Color.White.copy(alpha = 0.15f),
            backgroundOverlay = Color.Black.copy(alpha = 0.20f),
            accent = Color(0xFF00E5FF),
            borderColor = Color.White.copy(alpha = 0.40f)
        ),
        // 2. Customize Global Default Blur Ratio
        blurRadius = 24.dp,
        // 3. Customize Shapes and Corner Radiuses
        shapes = GlassShapes(
            cardShape = RoundedCornerShape(32.dp),
            surfaceShape = RoundedCornerShape(20.dp),
            buttonShape = RoundedCornerShape(16.dp),
            cornerRadius = 24.dp
        ),
        // 4. Customize Glass Refraction and Lens Dispersion
        refraction = GlassRefraction(
            depth = 16.dp,
            chromaticAberration = true,
            highlightAlpha = 0.8f
        ),
        content = content
    )
}
```

#### 2. Structured Preset Configuration via `GlassThemeConfig`

For design systems, define reusable preset configurations:

```kotlin
val MyCustomGlassConfig = GlassThemeConfig.dark(
    surfaceTint = Color(0x3300FFFF),
    accent = Color(0xFFFF4081),
    borderColor = Color(0x80FFFFFF),
    blurRadius = 30.dp,
    shapes = GlassShapes(cornerRadius = 18.dp),
    refraction = GlassRefraction(depth = 14.dp, chromaticAberration = true)
)

@Composable
fun App() {
    GlassTheme(config = MyCustomGlassConfig) {
        // All child components inherit MyCustomGlassConfig
    }
}
```

#### 3. Per-Component Parameter Overrides

Individual components allow overriding specific parameters on demand:

```kotlin
GlassCard(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),            // Custom corner shape
    blurRadius = 12.dp,                           // Custom blur radius
    tint = Color.Cyan.copy(alpha = 0.2f)          // Custom surface tint
) {
    Text("Customized GlassCard")
}
```

#### 4. Low-Level Shader & Refraction Customization (`drawBackdrop`)

Apply custom refraction depth, dispersion, and blur shaders directly:

```kotlin
val backdrop = requireGlassBackdrop()

Box(
    modifier = Modifier
        .fillMaxWidth()
        .drawBackdrop(
            backdrop = backdrop,
            shape = { RoundedCornerShape(24.dp) },
            effects = {
                vibrancy()                                        // Boost color saturation
                blur(20.dp.toPx())                                // Custom blur
                lens(
                    refractionHeight = 16.dp.toPx(),              // Refraction depth
                    refractionAmount = 32.dp.toPx(),              // Displacement amount
                    chromaticAberration = true                     // Color dispersion
                )
            },
            highlight = { Highlight.Plain }
        )
) {
    Text("Low-Level Shader & Refraction Effect")
}
```

---

## Component Overview

| Component | Description | Default Theme Attributes Used |
| :--- | :--- | :--- |
| **`GlassContainer`** | Root layout capturing background content for children | — |
| **`GlassCard`** | Standard frosted glass card container | `shapes.cardShape`, `blurRadius`, `colors.surfaceTint` |
| **`GlassSurface`** | Customizable glass surface with blur | `shapes.surfaceShape`, `blurRadius`, `colors.surfaceTint` |
| **`GlassButton`** | Tactile frosted glass button | `shapes.buttonShape`, `colors.accent`, `refraction` |
| **`GlassBlur`** | Direct backdrop blur region modifier | `blurRadius` |
| **`GlassTheme`** | Global styling context | `colors`, `blurRadius`, `shapes`, `refraction` |

---

## Sample / Demo App

A complete runnable sample app is included in the `:app` module. To launch it:
1. Open the project in Android Studio.
2. Select the `app` run configuration and click **Run**.
3. Choose **"View Code Examples 💡"** to inspect live parameter customization samples.

---

## Publishing Instructions

For instructions on publishing GlassKit to Maven Central, JitPack, or Maven Local, see [PUBLISHING.md](./PUBLISHING.md).

---

## License

This project is distributed under the Apache License, Version 2.0. See [LICENSE](./LICENSE) for details.
