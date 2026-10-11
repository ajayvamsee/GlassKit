package io.github.ajayvamsee.glasskit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import io.github.ajayvamsee.glasskit.backdrops.rememberLayerBackdrop
import io.github.ajayvamsee.glasskit.effects.blur
import io.github.ajayvamsee.glasskit.effects.vibrancy
import io.github.ajayvamsee.glasskit.internal.LocalGlassBackdrop
import io.github.ajayvamsee.glasskit.internal.requireGlassBackdrop
import io.github.ajayvamsee.glasskit.modifiers.layerBackdrop
import io.github.ajayvamsee.glasskit.theme.GlassTheme

/**
 * Captures a composable background and makes it available to glass components in [content].
 *
 * Place the backdrop-producing [background] before the glass content in the same container.
 * Backdrop capture is shared for all components in this container.
 *
 * @param modifier modifier applied to the container.
 * @param background composable background that is displayed and captured for glass sampling.
 * @param content glass content drawn over the captured background.
 */
@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = rememberLayerBackdrop()
    Box(modifier) {
        Box(
            Modifier.matchParentSize().layerBackdrop(backdrop),
            content = background
        )
        CompositionLocalProvider(LocalGlassBackdrop provides backdrop) {
            content()
        }
    }
}

/**
 * A customizable glass surface that samples the nearest [GlassContainer] background.
 *
 * Defaults for [shape], [blurRadius], and [tint] are automatically configured from [GlassTheme].
 *
 * @param modifier modifier applied to the surface.
 * @param shape surface outline; defaults to [GlassTheme.shapes.surfaceShape].
 * @param blurRadius blur radius in density-independent pixels; defaults to [GlassTheme.blurRadius].
 * @param tint translucent color drawn over the sampled backdrop; defaults to [GlassTheme.colors.surfaceTint].
 * @param content composable content placed inside the surface.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = GlassTheme.shapes.surfaceShape,
    blurRadius: Dp = GlassTheme.blurRadius,
    tint: Color = GlassTheme.colors.surfaceTint,
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = requireGlassBackdrop()
    LiquidSurface(
        backdrop = backdrop,
        modifier = modifier,
        shape = { shape },
        effects = {
            vibrancy()
            if (blurRadius > Dp.Unspecified && blurRadius.value > 0f) {
                blur(blurRadius.toPx())
            }
        },
        onDrawSurface = { drawRect(tint) },
        content = content
    )
}

/**
 * A rounded glass card for grouping related content.
 *
 * Defaults for [shape], [blurRadius], and [tint] are automatically configured from [GlassTheme].
 *
 * @param modifier modifier applied to the card.
 * @param shape card outline; defaults to [GlassTheme.shapes.cardShape].
 * @param blurRadius blur radius in density-independent pixels; defaults to [GlassTheme.blurRadius].
 * @param tint translucent color drawn over the sampled backdrop; defaults to [GlassTheme.colors.surfaceTint].
 * @param content composable card content.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = GlassTheme.shapes.cardShape,
    blurRadius: Dp = GlassTheme.blurRadius,
    tint: Color = GlassTheme.colors.surfaceTint,
    content: @Composable BoxScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier,
        shape = shape,
        blurRadius = blurRadius,
        tint = tint,
        content = content
    )
}

/**
 * A glass button with press feedback and standard button semantics.
 *
 * Defaults for [shape] and [tint] are automatically configured from [GlassTheme].
 *
 * @param onClick callback invoked when the button is activated.
 * @param modifier modifier applied to the button.
 * @param shape button outline; defaults to [GlassTheme.shapes.buttonShape].
 * @param tint optional glass hue; defaults to [GlassTheme.colors.accent].
 * @param content composable button content.
 */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = GlassTheme.shapes.buttonShape,
    tint: Color = GlassTheme.colors.accent,
    content: @Composable RowScope.() -> Unit
) {
    LiquidButton(
        onClick = onClick,
        backdrop = requireGlassBackdrop(),
        modifier = modifier,
        shape = shape,
        tint = tint,
        content = content
    )
}
