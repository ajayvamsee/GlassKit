package io.github.ajayvamsee.glasskit.shadow

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Parameters for an outer shadow rendered around a glass surface.
 *
 * @property radius shadow blur radius.
 * @property offset shadow displacement from the surface.
 * @property color shadow color.
 * @property alpha overall shadow opacity.
 * @property blendMode blend mode used to composite the shadow.
 */
@Immutable
data class Shadow(
    val radius: Dp = 24f.dp,
    val offset: DpOffset = DpOffset(0f.dp, radius / 6f),
    val color: Color = Color.Black.copy(alpha = 0.1f),
    @param:FloatRange(from = 0.0, to = 1.0) val alpha: Float = 1f,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode
) {

    companion object {

        /** Default soft shadow for glass surfaces. */
        @Stable
        val Default: Shadow = Shadow()
    }
}
