package io.github.ajayvamsee.glasskit.blur

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import io.github.ajayvamsee.glasskit.modifiers.drawPlainBackdrop
import io.github.ajayvamsee.glasskit.effects.blur
import io.github.ajayvamsee.glasskit.internal.requireGlassBackdrop

/**
 * Blurs the nearest [io.github.ajayvamsee.glasskit.components.GlassContainer] background.
 *
 * Blur is supported on Android 12 (API 31) and newer; older versions draw the unblurred
 * backdrop while retaining the content and layout.
 *
 * @param modifier modifier applied to the blur region.
 * @param radius blur radius in density-independent pixels; set to zero for no blur.
 * @param shape clipping shape for the sampled backdrop.
 * @param content composable content drawn above the blurred backdrop.
 */
@Composable
fun GlassBlur(
    modifier: Modifier = Modifier,
    radius: Dp,
    shape: Shape = RectangleShape,
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = requireGlassBackdrop()
    Box(
        modifier.drawPlainBackdrop(
            backdrop = backdrop,
            shape = { shape },
            effects = { blur(radius.toPx()) }
        ),
        content = content
    )
}
