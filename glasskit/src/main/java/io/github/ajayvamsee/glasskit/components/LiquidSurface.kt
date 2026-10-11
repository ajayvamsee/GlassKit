package io.github.ajayvamsee.glasskit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.github.ajayvamsee.glasskit.Backdrop
import io.github.ajayvamsee.glasskit.BackdropEffectScope
import io.github.ajayvamsee.glasskit.backdrops.LayerBackdrop
import io.github.ajayvamsee.glasskit.modifiers.drawBackdrop
import io.github.ajayvamsee.glasskit.highlight.Highlight
import io.github.ajayvamsee.glasskit.shadow.InnerShadow
import io.github.ajayvamsee.glasskit.shadow.Shadow
import com.kyant.shapes.Capsule

private val DefaultShape: () -> Shape = { Capsule() }

/**
 * Draws content inside a customizable backdrop surface.
 *
 * @param backdrop backdrop source sampled by the surface.
 * @param modifier modifier applied to the surface.
 * @param shape shape provider for clipping and effects.
 * @param effects effect operations applied to the backdrop.
 * @param highlight optional edge highlight.
 * @param shadow optional outer shadow.
 * @param innerShadow optional inner shadow.
 * @param layerBlock optional graphics-layer transform.
 * @param exportedBackdrop optional destination for recording this surface as a backdrop.
 * @param onDrawBehind optional drawing performed behind the backdrop.
 * @param onDrawBackdrop custom drawing operation for the sampled backdrop.
 * @param onDrawSurface optional surface overlay drawing.
 * @param onDrawFront optional drawing performed over the content.
 * @param contentAlignment alignment of the content within the surface.
 * @param propagateMinConstraints whether the surface propagates minimum constraints to content.
 * @param content content placed within the surface.
 */
@Composable
fun LiquidSurface(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    shape: () -> Shape = DefaultShape,
    effects: BackdropEffectScope.() -> Unit = {},
    highlight: (() -> Highlight?)? = { Highlight.Default },
    shadow: (() -> Shadow?)? = { Shadow.Default },
    innerShadow: (() -> InnerShadow?)? = null,
    layerBlock: (GraphicsLayerScope.() -> Unit)? = null,
    exportedBackdrop: LayerBackdrop? = null,
    onDrawBehind: (DrawScope.() -> Unit)? = null,
    onDrawBackdrop: DrawScope.(drawBackdrop: DrawScope.() -> Unit) -> Unit = { it() },
    onDrawSurface: (DrawScope.() -> Unit)? = null,
    onDrawFront: (DrawScope.() -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.drawBackdrop(
            backdrop = backdrop,
            shape = shape,
            effects = effects,
            highlight = highlight,
            shadow = shadow,
            innerShadow = innerShadow,
            layerBlock = layerBlock,
            exportedBackdrop = exportedBackdrop,
            onDrawBehind = onDrawBehind,
            onDrawBackdrop = onDrawBackdrop,
            onDrawSurface = onDrawSurface,
            onDrawFront = onDrawFront
        ),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}
