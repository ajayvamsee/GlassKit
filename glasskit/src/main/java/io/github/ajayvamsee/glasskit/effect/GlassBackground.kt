package io.github.ajayvamsee.glasskit.effect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.ajayvamsee.glasskit.Backdrop
import io.github.ajayvamsee.glasskit.internal.LocalGlassBackdrop
import io.github.ajayvamsee.glasskit.internal.requireGlassBackdrop
import io.github.ajayvamsee.glasskit.theme.GlassTheme

/**
 * Draws a translucent overlay and provides a backdrop to nested GlassKit components.
 *
 * Use this when a backdrop is already available from [io.github.ajayvamsee.glasskit.components.GlassContainer]
 * or when supplying one explicitly.
 *
 * @param modifier modifier applied to the overlay.
 * @param backdrop optional backdrop to provide; defaults to the enclosing glass container's backdrop.
 * @param color overlay color; defaults to the theme's background overlay.
 * @param content composable content drawn over the overlay.
 */
@Composable
fun GlassBackground(
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null,
    color: Color = GlassTheme.colors.backgroundOverlay,
    content: @Composable BoxScope.() -> Unit
) {
    val resolvedBackdrop = backdrop ?: requireGlassBackdrop()
    Box(modifier.background(color)) {
        CompositionLocalProvider(LocalGlassBackdrop provides resolvedBackdrop) {
            content()
        }
    }
}
