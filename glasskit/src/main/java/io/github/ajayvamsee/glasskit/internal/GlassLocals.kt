package io.github.ajayvamsee.glasskit.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.ajayvamsee.glasskit.Backdrop

val LocalGlassBackdrop = staticCompositionLocalOf<Backdrop?> { null }

@Composable
fun requireGlassBackdrop(): Backdrop =
    checkNotNull(LocalGlassBackdrop.current) {
        "GlassKit components must be used inside GlassContainer or GlassBackground."
    }
