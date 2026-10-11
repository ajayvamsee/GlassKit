package io.github.ajayvamsee.glasskit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.shapes.Capsule

/**
 * GlassKit color tokens used by its high-level components.
 *
 * @property surfaceTint translucent tint drawn over glass surfaces.
 * @property backgroundOverlay color used by [io.github.ajayvamsee.glasskit.effect.GlassBackground].
 * @property accent tint used by glass buttons, active sliders, and toggles.
 * @property borderColor border/highlight accent color applied to glass outlines.
 */
@Immutable
data class GlassColors(
    val surfaceTint: Color,
    val backgroundOverlay: Color,
    val accent: Color,
    val borderColor: Color = Color.White.copy(alpha = 0.35f)
)

private val LightGlassColors = GlassColors(
    surfaceTint = Color.White.copy(alpha = 0.40f),
    backgroundOverlay = Color.White.copy(alpha = 0.08f),
    accent = Color(0xFF0284C7),
    borderColor = Color.White.copy(alpha = 0.55f)
)

private val DarkGlassColors = GlassColors(
    surfaceTint = Color.White.copy(alpha = 0.10f),
    backgroundOverlay = Color.Black.copy(alpha = 0.12f),
    accent = Color(0xFF38BDF8),
    borderColor = Color.White.copy(alpha = 0.22f)
)

/**
 * Refraction and lens effect settings for GlassKit surfaces and liquid components.
 *
 * @property depth height or depth multiplier for the refraction lens effect in Dp.
 * @property chromaticAberration whether to apply color dispersion on glass edges.
 * @property highlightAlpha opacity multiplier for specular highlights.
 */
@Immutable
data class GlassRefraction(
    val depth: Dp = 12.dp,
    val chromaticAberration: Boolean = true,
    val highlightAlpha: Float = 0.6f
)

/**
 * Shape and corner settings for GlassKit components.
 *
 * @property cardShape default shape for [io.github.ajayvamsee.glasskit.components.GlassCard].
 * @property surfaceShape default shape for [io.github.ajayvamsee.glasskit.components.GlassSurface].
 * @property buttonShape default shape for [io.github.ajayvamsee.glasskit.components.GlassButton].
 * @property cornerRadius default corner radius for rounded shapes.
 */
@Immutable
data class GlassShapes(
    val cardShape: Shape = RoundedCornerShape(28.dp),
    val surfaceShape: Shape = RoundedCornerShape(24.dp),
    val buttonShape: Shape = Capsule(),
    val cornerRadius: Dp = 24.dp
)

/**
 * Global or context-local configuration for GlassKit styling, blur, shapes, and refraction.
 *
 * @property colors palette tokens used for surfaces, overlays, accents, and borders.
 * @property blurRadius default backdrop blur radius in density-independent pixels.
 * @property shapes default component shapes and corner radius configurations.
 * @property refraction refraction depth, chromatic dispersion, and highlight intensity.
 */
@Immutable
data class GlassThemeConfig(
    val colors: GlassColors,
    val blurRadius: Dp = 16.dp,
    val shapes: GlassShapes = GlassShapes(),
    val refraction: GlassRefraction = GlassRefraction()
) {
    companion object {
        fun light(
            surfaceTint: Color = LightGlassColors.surfaceTint,
            backgroundOverlay: Color = LightGlassColors.backgroundOverlay,
            accent: Color = LightGlassColors.accent,
            borderColor: Color = LightGlassColors.borderColor,
            blurRadius: Dp = 16.dp,
            shapes: GlassShapes = GlassShapes(),
            refraction: GlassRefraction = GlassRefraction()
        ): GlassThemeConfig = GlassThemeConfig(
            colors = GlassColors(
                surfaceTint = surfaceTint,
                backgroundOverlay = backgroundOverlay,
                accent = accent,
                borderColor = borderColor
            ),
            blurRadius = blurRadius,
            shapes = shapes,
            refraction = refraction
        )

        fun dark(
            surfaceTint: Color = DarkGlassColors.surfaceTint,
            backgroundOverlay: Color = DarkGlassColors.backgroundOverlay,
            accent: Color = DarkGlassColors.accent,
            borderColor: Color = DarkGlassColors.borderColor,
            blurRadius: Dp = 16.dp,
            shapes: GlassShapes = GlassShapes(),
            refraction: GlassRefraction = GlassRefraction()
        ): GlassThemeConfig = GlassThemeConfig(
            colors = GlassColors(
                surfaceTint = surfaceTint,
                backgroundOverlay = backgroundOverlay,
                accent = accent,
                borderColor = borderColor
            ),
            blurRadius = blurRadius,
            shapes = shapes,
            refraction = refraction
        )
    }
}

private val LocalGlassThemeConfig = staticCompositionLocalOf { GlassThemeConfig.light() }
private val LocalGlassColors = staticCompositionLocalOf { LightGlassColors }

/**
 * Theme provider and access to GlassKit's current theme values.
 */
object GlassTheme {
    /**
     * Provides light or dark Material 3 and GlassKit color schemes and component styling to its content.
     *
     * @param darkTheme whether to use the dark palette; defaults to the system setting.
     * @param colors optional custom color tokens overriding the default theme palette.
     * @param blurRadius optional default blur radius applied to descendant glass components.
     * @param shapes optional shape definitions overriding card, surface, and button shapes.
     * @param refraction optional refraction and lens parameters.
     * @param config complete configuration object; defaults to [GlassThemeConfig.dark] or [GlassThemeConfig.light].
     * @param content composable content that consumes the theme.
     */
    @Composable
    operator fun invoke(
        darkTheme: Boolean = isSystemInDarkTheme(),
        colors: GlassColors? = null,
        blurRadius: Dp? = null,
        shapes: GlassShapes? = null,
        refraction: GlassRefraction? = null,
        config: GlassThemeConfig = if (darkTheme) GlassThemeConfig.dark() else GlassThemeConfig.light(),
        content: @Composable () -> Unit
    ) {
        val baseConfig = if (darkTheme && (config == GlassThemeConfig.light())) GlassThemeConfig.dark() else config
        val finalConfig = baseConfig.copy(
            colors = colors ?: baseConfig.colors,
            blurRadius = blurRadius ?: baseConfig.blurRadius,
            shapes = shapes ?: baseConfig.shapes,
            refraction = refraction ?: baseConfig.refraction
        )

        CompositionLocalProvider(
            LocalGlassThemeConfig provides finalConfig,
            LocalGlassColors provides finalConfig.colors
        ) {
            MaterialTheme(
                colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
                content = content
            )
        }
    }

    /** The current full GlassKit configuration. */
    val config: GlassThemeConfig
        @Composable get() = LocalGlassThemeConfig.current

    /** The current GlassKit palette. */
    val colors: GlassColors
        @Composable get() = LocalGlassThemeConfig.current.colors

    /** The current default blur radius. */
    val blurRadius: Dp
        @Composable get() = LocalGlassThemeConfig.current.blurRadius

    /** The current component shapes. */
    val shapes: GlassShapes
        @Composable get() = LocalGlassThemeConfig.current.shapes

    /** The current refraction and lens settings. */
    val refraction: GlassRefraction
        @Composable get() = LocalGlassThemeConfig.current.refraction
}
