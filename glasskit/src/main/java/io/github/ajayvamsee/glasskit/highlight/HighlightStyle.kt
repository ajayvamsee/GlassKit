package io.github.ajayvamsee.glasskit.highlight

import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastCoerceAtMost
import io.github.ajayvamsee.glasskit.AmbientHighlightShaderString
import io.github.ajayvamsee.glasskit.DefaultHighlightShaderString
import io.github.ajayvamsee.glasskit.RuntimeShaderCache
import kotlin.math.PI

/**
 * Defines the color, blend mode, and shader for a surface highlight.
 */
@Immutable
interface HighlightStyle {

    /** Base highlight color. */
    val color: Color

    /** Blend mode used when drawing the highlight. */
    val blendMode: BlendMode

    /**
     * Creates the runtime shader for this style when the platform supports it.
     *
     * @param shape surface shape used to calculate shader geometry.
     * @param runtimeShaderCache cache used to reuse compiled shaders.
     * @return the shader, or `null` when no runtime shader is required or supported.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun DrawScope.createShader(
        shape: Shape,
        runtimeShaderCache: RuntimeShaderCache
    ): Shader?

    /**
     * Flat highlight rendered using [color] and [blendMode].
     *
     * @property color highlight color.
     * @property blendMode highlight blend mode.
     */
    @Immutable
    data class Plain(
        override val color: Color = Color.White.copy(alpha = 0.38f),
        override val blendMode: BlendMode = BlendMode.Plus
    ) : HighlightStyle {

        @RequiresApi(Build.VERSION_CODES.S)
        override fun DrawScope.createShader(
            shape: Shape,
            runtimeShaderCache: RuntimeShaderCache
        ): Shader? = null
    }

    /**
     * Directional highlight with configurable angle and edge falloff.
     *
     * @property color highlight color.
     * @property blendMode highlight blend mode.
     * @property angle direction of the highlight in degrees.
     * @property falloff edge intensity falloff.
     */
    @Immutable
    data class Default(
        override val color: Color = Color.White.copy(alpha = 0.5f),
        override val blendMode: BlendMode = BlendMode.Plus,
        val angle: Float = 45f,
        @param:FloatRange(from = 0.0) val falloff: Float = 1f
    ) : HighlightStyle {

        @Deprecated(
            "Use the constructor with color parameter instead.",
            ReplaceWith("HighlightStyle.Default(color = Color.White.copy(alpha = intensity), angle = angle, falloff = falloff)")
        )
        constructor(
            intensity: Float,
            angle: Float = 45f,
            falloff: Float = 1f
        ) : this(
            color = Color.White.copy(alpha = intensity),
            angle = angle,
            falloff = falloff
        )

        @RequiresApi(Build.VERSION_CODES.S)
        override fun DrawScope.createShader(
            shape: Shape,
            runtimeShaderCache: RuntimeShaderCache
        ): Shader? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                runtimeShaderCache.obtainRuntimeShader(
                    "Default",
                    DefaultHighlightShaderString
                ).apply {
                    setFloatUniform("size", size.width, size.height)
                    setFloatUniform("cornerRadii", getCornerRadii(shape))
                    setColorUniform("color", color.copy(alpha = 1f).toArgb())
                    setFloatUniform("angle", angle * (PI / 180f).toFloat())
                    setFloatUniform("falloff", falloff)
                }
            } else {
                null
            }
        }
    }

    /**
     * Uniform ambient highlight without a directional shader.
     *
     * @property intensity highlight opacity.
     */
    @Immutable
    data class Ambient(
        @param:FloatRange(from = 0.0, to = 1.0) val intensity: Float = 0.38f
    ) : HighlightStyle {

        override val color: Color = Color.White.copy(alpha = intensity)

        override val blendMode: BlendMode = DrawScope.DefaultBlendMode

        @RequiresApi(Build.VERSION_CODES.S)
        override fun DrawScope.createShader(
            shape: Shape,
            runtimeShaderCache: RuntimeShaderCache
        ): Shader? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                runtimeShaderCache.obtainRuntimeShader(
                    "Ambient",
                    AmbientHighlightShaderString
                ).apply {
                    setFloatUniform("size", size.width, size.height)
                    setFloatUniform("cornerRadii", getCornerRadii(shape))
                    setFloatUniform("angle", 45f * (PI / 180f).toFloat())
                    setFloatUniform("falloff", 1f)
                }
            } else {
                null
            }
        }
    }

    companion object {

        /** Default directional style. */
        @Stable
        val Default: Default = Default()

        /** Ambient style. */
        @Stable
        val Ambient: Ambient = Ambient()

        /** Plain blend-only style. */
        @Stable
        val Plain: Plain = Plain()
    }
}

private fun DrawScope.getCornerRadii(shape: Shape): FloatArray {
    val size = size
    val maxRadius = size.minDimension / 2f
    val shape = shape as? CornerBasedShape ?: return FloatArray(4) { maxRadius }
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val topLeft =
        if (isLtr) shape.topStart.toPx(size, this)
        else shape.topEnd.toPx(size, this)
    val topRight =
        if (isLtr) shape.topEnd.toPx(size, this)
        else shape.topStart.toPx(size, this)
    val bottomRight =
        if (isLtr) shape.bottomEnd.toPx(size, this)
        else shape.bottomStart.toPx(size, this)
    val bottomLeft =
        if (isLtr) shape.bottomStart.toPx(size, this)
        else shape.bottomEnd.toPx(size, this)
    return floatArrayOf(
        topLeft.fastCoerceAtMost(maxRadius),
        topRight.fastCoerceAtMost(maxRadius),
        bottomRight.fastCoerceAtMost(maxRadius),
        bottomLeft.fastCoerceAtMost(maxRadius)
    )
}
