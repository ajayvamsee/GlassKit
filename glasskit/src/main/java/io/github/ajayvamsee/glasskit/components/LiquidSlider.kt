package io.github.ajayvamsee.glasskit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.util.lerp
import io.github.ajayvamsee.glasskit.Backdrop
import io.github.ajayvamsee.glasskit.modifiers.layerBackdrop
import io.github.ajayvamsee.glasskit.backdrops.rememberBackdrop
import io.github.ajayvamsee.glasskit.backdrops.rememberCombinedBackdrop
import io.github.ajayvamsee.glasskit.backdrops.rememberLayerBackdrop
import io.github.ajayvamsee.glasskit.animation.DampedDragAnimation
import io.github.ajayvamsee.glasskit.effects.blur
import io.github.ajayvamsee.glasskit.effects.lens
import io.github.ajayvamsee.glasskit.highlight.Highlight
import io.github.ajayvamsee.glasskit.shadow.InnerShadow
import io.github.ajayvamsee.glasskit.shadow.Shadow
import com.kyant.shapes.Capsule
import kotlinx.coroutines.flow.collectLatest

/**
 * Renders an animated glass slider backed by a backdrop.
 *
 * @param value current value supplied by the caller.
 * @param onValueChange callback for user changes.
 * @param valueRange inclusive range of selectable values.
 * @param visibilityThreshold minimum animated change considered visible.
 * @param backdrop backdrop source sampled by the slider.
 * @param modifier modifier applied to the slider.
 * @param thumbContent content drawn inside the slider thumb.
 */
@Composable
fun LiquidSlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    visibilityThreshold: Float,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    thumbContent: @Composable BoxScope.() -> Unit = {}
) {
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor = if (isLightTheme) Color(0xFF0088FF) else Color(0xFF0091FF)
    val trackColor =
        if (isLightTheme) Color(0xFF787878).copy(0.2f) else Color(0xFF787880).copy(0.36f)

    val trackBackdrop = rememberLayerBackdrop()

    BoxWithConstraints(modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        val trackWidth = constraints.maxWidth
        val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
        val animationScope = rememberCoroutineScope()
        var didDrag by remember { mutableStateOf(false) }
        val dampedDragAnimation = remember(animationScope) {
            DampedDragAnimation(
                animationScope = animationScope,
                initialValue = value(),
                valueRange = valueRange,
                visibilityThreshold = visibilityThreshold,
                initialScale = 1f,
                pressedScale = 1.5f,
                onDragStarted = {},
                onDragStopped = {
                    if (didDrag) {
                        onValueChange(targetValue)
                    }
                },
                onDrag = { _, dragAmount ->
                    if (!didDrag) {
                        didDrag = dragAmount.x != 0f
                    }
                    val delta =
                        (valueRange.endInclusive - valueRange.start) * (dragAmount.x / trackWidth)
                    onValueChange(
                        if (isLtr) (targetValue + delta).coerceIn(valueRange)
                        else (targetValue - delta).coerceIn(valueRange)
                    )
                }
            )
        }
        LaunchedEffect(dampedDragAnimation) {
            snapshotFlow { value() }
                .collectLatest { currentValue ->
                    if (dampedDragAnimation.targetValue != currentValue) {
                        dampedDragAnimation.updateValue(currentValue)
                    }
                }
        }

        Box(Modifier.layerBackdrop(trackBackdrop)) {
            Box(
                Modifier
                    .clip(Capsule())
                    .background(trackColor)
                    .pointerInput(animationScope) {
                        detectTapGestures { position ->
                            val delta =
                                (valueRange.endInclusive - valueRange.start) * (position.x / trackWidth)
                            val targetValue =
                                (if (isLtr) valueRange.start + delta else valueRange.endInclusive - delta)
                                    .coerceIn(valueRange)
                            dampedDragAnimation.animateToValue(targetValue)
                            onValueChange(targetValue)
                        }
                    }
                    .height(6f.dp)
                    .fillMaxWidth()
            )

            Box(
                Modifier
                    .clip(Capsule())
                    .background(accentColor)
                    .height(6f.dp)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val width = (constraints.maxWidth * dampedDragAnimation.progress).fastRoundToInt()
                        layout(width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            )
        }

        LiquidSurface(
            backdrop = rememberCombinedBackdrop(
                backdrop,
                rememberBackdrop(trackBackdrop) { drawBackdrop ->
                    val progress = dampedDragAnimation.pressProgress
                    val scaleX = lerp(2f / 3f, 1f, progress)
                    val scaleY = lerp(0f, 1f, progress)
                    scale(scaleX, scaleY) { drawBackdrop() }
                }
            ),
            modifier = Modifier
                .graphicsLayer {
                    translationX =
                        (-size.width / 2f + trackWidth * dampedDragAnimation.progress)
                            .fastCoerceIn(-size.width / 4f, trackWidth - size.width * 3f / 4f) *
                            if (isLtr) 1f else -1f
                }
                .then(dampedDragAnimation.modifier)
                .size(40f.dp, 24f.dp),
            shape = { Capsule() },
            effects = {
                val progress = dampedDragAnimation.pressProgress
                blur(8f.dp.toPx() * (1f - progress))
                lens(
                    10f.dp.toPx() * progress,
                    14f.dp.toPx() * progress,
                    chromaticAberration = true
                )
            },
            highlight = {
                val progress = dampedDragAnimation.pressProgress
                Highlight.Ambient.copy(
                    width = Highlight.Ambient.width / 1.5f,
                    blurRadius = Highlight.Ambient.blurRadius / 1.5f,
                    alpha = progress
                )
            },
            shadow = {
                Shadow(radius = 4f.dp, color = Color.Black.copy(alpha = 0.05f))
            },
            innerShadow = {
                val progress = dampedDragAnimation.pressProgress
                InnerShadow(radius = 4f.dp * progress, alpha = progress)
            },
            layerBlock = {
                scaleX = dampedDragAnimation.scaleX
                scaleY = dampedDragAnimation.scaleY
                val velocity = dampedDragAnimation.velocity / 10f
                scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
            },
            onDrawSurface = {
                val progress = dampedDragAnimation.pressProgress
                drawRect(Color.White.copy(alpha = 1f - progress))
            },
            content = thumbContent
        )
    }
}
