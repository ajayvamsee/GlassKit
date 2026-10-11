package io.github.ajayvamsee.glasskit.effects

import android.graphics.RenderEffect
import android.os.Build
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toAndroidTileMode
import io.github.ajayvamsee.glasskit.BackdropEffectScope

/**
 * Adds a Gaussian blur to the current backdrop effect chain.
 *
 * Blur is available on Android 12 (API 31) and newer.
 *
 * @param radius blur radius in pixels; non-positive values do not add an effect.
 * @param edgeTreatment treatment applied when sampling beyond the input bounds.
 */
fun BackdropEffectScope.blur(
    @FloatRange(from = 0.0) radius: Float,
    edgeTreatment: TileMode = TileMode.Clamp
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
    if (radius <= 0f) return

    if (edgeTreatment != TileMode.Clamp || renderEffect != null) {
        if (radius > padding) {
            padding = radius
        }
    }

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                currentEffect,
                edgeTreatment.toAndroidTileMode()
            )
        } else {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                edgeTreatment.toAndroidTileMode()
            )
        }
}
