package io.github.ajayvamsee.glasskit.effects

import android.graphics.RenderEffect
import android.os.Build
import io.github.ajayvamsee.glasskit.BackdropEffectScope

/**
 * Appends an Android render effect to the backdrop effect chain.
 *
 * @param effect platform effect to append.
 */
fun BackdropEffectScope.effect(effect: RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createChainEffect(effect, currentEffect)
        } else {
            effect
        }
}

/**
 * Appends a Compose render effect to the backdrop effect chain.
 *
 * @param effect Compose effect to append.
 */
fun BackdropEffectScope.effect(effect: androidx.compose.ui.graphics.RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    effect(effect.asAndroidRenderEffect())
}
