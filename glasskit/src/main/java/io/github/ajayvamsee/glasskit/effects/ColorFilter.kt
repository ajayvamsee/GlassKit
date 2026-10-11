package io.github.ajayvamsee.glasskit.effects

import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.os.Build
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.asAndroidColorFilter
import io.github.ajayvamsee.glasskit.BackdropEffectScope
import io.github.ajayvamsee.glasskit.GammaAdjustmentShaderString
import kotlin.math.pow

/** Adds an Android color filter to the current effect chain. */
fun BackdropEffectScope.colorFilter(colorFilter: ColorFilter) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createColorFilterEffect(colorFilter, currentEffect)
        } else {
            RenderEffect.createColorFilterEffect(colorFilter)
        }
}

/** Adds a Compose color filter to the current effect chain. */
fun BackdropEffectScope.colorFilter(colorFilter: androidx.compose.ui.graphics.ColorFilter) {
    colorFilter(colorFilter.asAndroidColorFilter())
}

/**
 * Adjusts backdrop opacity.
 *
 * @param alpha output alpha in the inclusive range from zero to one.
 */
fun BackdropEffectScope.opacity(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, alpha, 0f
        )
    )
    colorFilter(ColorMatrixColorFilter(colorMatrix))
}

/**
 * Adjusts backdrop brightness, contrast, and saturation.
 *
 * @param brightness brightness offset, where zero leaves brightness unchanged.
 * @param contrast contrast multiplier, where one leaves contrast unchanged.
 * @param saturation saturation multiplier, where one leaves saturation unchanged.
 */
fun BackdropEffectScope.colorControls(
    brightness: Float = 0f,
    contrast: Float = 1f,
    saturation: Float = 1f
) {
    if (brightness == 0f && contrast == 1f && saturation == 1f) {
        return
    }

    colorFilter(colorControlsColorFilter(brightness, contrast, saturation))
}

/** Increases backdrop color saturation for a more vivid glass appearance. */
fun BackdropEffectScope.vibrancy() {
    colorFilter(VibrantColorFilter)
}

private val VibrantColorFilter = colorControlsColorFilter(saturation = 1.5f)

/**
 * Adjusts backdrop exposure.
 *
 * @param ev exposure adjustment in stops.
 */
fun BackdropEffectScope.exposureAdjustment(ev: Float) {
    val scale = 2f.pow(ev / 2.2f)
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            scale, 0f, 0f, 0f, 0f,
            0f, scale, 0f, 0f, 0f,
            0f, 0f, scale, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    colorFilter(ColorMatrixColorFilter(colorMatrix))
}

/**
 * Applies gamma correction on Android 13 (API 33) and newer.
 *
 * @param power gamma exponent.
 */
fun BackdropEffectScope.gammaAdjustment(power: Float) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val shader = obtainRuntimeShader("GammaAdjustment", GammaAdjustmentShaderString).apply {
        setFloatUniform("power", power)
    }
    effect(RenderEffect.createRuntimeShaderEffect(shader, "content"))
}

private fun colorControlsColorFilter(
    brightness: Float = 0f,
    contrast: Float = 1f,
    saturation: Float = 1f
): ColorFilter {
    val invSat = 1f - saturation
    val r = 0.213f * invSat
    val g = 0.715f * invSat
    val b = 0.072f * invSat

    val c = contrast
    val t = (0.5f - c * 0.5f + brightness) * 255f
    val s = saturation

    val cr = c * r
    val cg = c * g
    val cb = c * b
    val cs = c * s

    val colorMatrix = ColorMatrix(
        floatArrayOf(
            cr + cs, cg, cb, 0f, t,
            cr, cg + cs, cb, 0f, t,
            cr, cg, cb + cs, 0f, t,
            0f, 0f, 0f, 1f, 0f
        )
    )
    return ColorMatrixColorFilter(colorMatrix)
}
