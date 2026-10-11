package io.github.ajayvamsee.glasskit

import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density

/**
 * A drawing source sampled by a GlassKit backdrop modifier or surface.
 *
 * Implementations draw into the caller's draw scope and may use layout coordinates for
 * position-dependent sampling.
 */
interface Backdrop {

    /** Whether drawing depends on the backdrop's position in the layout tree. */
    val isCoordinatesDependent: Boolean

    /**
     * Draws this backdrop into the current draw scope.
     *
     * @param density density and font scale used for rendering.
     * @param coordinates coordinates of the consuming layout, when available.
     * @param layerBlock optional transform applied to the backdrop layer.
     */
    fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)? = null
    )
}
