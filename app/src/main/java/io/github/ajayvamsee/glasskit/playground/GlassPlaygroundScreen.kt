package io.github.ajayvamsee.glasskit.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ajayvamsee.glasskit.components.GlassButton
import io.github.ajayvamsee.glasskit.components.GlassCard
import io.github.ajayvamsee.glasskit.components.GlassContainer
import io.github.ajayvamsee.glasskit.effects.blur
import io.github.ajayvamsee.glasskit.effects.lens
import io.github.ajayvamsee.glasskit.effects.vibrancy
import io.github.ajayvamsee.glasskit.highlight.Highlight
import io.github.ajayvamsee.glasskit.internal.requireGlassBackdrop
import io.github.ajayvamsee.glasskit.modifiers.drawBackdrop
import io.github.ajayvamsee.glasskit.theme.GlassTheme
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlassPlaygroundScreen(
    onBackClick: () -> Unit
) {
    val animationScope = rememberCoroutineScope()
    val offsetAnimation = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val zoomAnimation = remember { Animatable(1f) }
    val rotationAnimation = remember { Animatable(0f) }

    var isSheetExpanded by rememberSaveable { mutableStateOf(true) }

    var cornerRadiusFrac by remember { mutableFloatStateOf(0.4f) }
    var blurRadiusDp by remember { mutableFloatStateOf(16f) }
    var refractionHeightFrac by remember { mutableFloatStateOf(0.25f) }
    var refractionAmountFrac by remember { mutableFloatStateOf(0.25f) }
    var chromaticAberration by remember { mutableFloatStateOf(1f) }

    GlassTheme(darkTheme = isSystemInDarkTheme()) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF581C87),
                                    Color(0xFF0284C7),
                                    Color(0xFF1E293B)
                                )
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 30.dp, top = 100.dp)
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color(0xE6F43F5E))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(Color(0xE610B981))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 40.dp, bottom = 120.dp)
                            .size(240.dp)
                            .clip(CircleShape)
                            .background(Color(0xE63B82F6))
                    )
                }
            }
        ) {
            val rootBackdrop = requireGlassBackdrop()

            Box(modifier = Modifier.fillMaxSize()) {
                // Interactive Transformable Glass Surface
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp)
                        .size(240.dp)
                        .drawBackdrop(
                            backdrop = rootBackdrop,
                            shape = {
                                val radiusDp = (120f * cornerRadiusFrac).dp
                                RoundedCornerShape(radiusDp)
                            },
                            effects = {
                                val minDimension = size.minDimension
                                vibrancy()
                                if (blurRadiusDp > 0f) {
                                    blur(blurRadiusDp.dp.toPx())
                                }
                                lens(
                                    refractionHeight = refractionHeightFrac * minDimension * 0.5f,
                                    refractionAmount = refractionAmountFrac * minDimension,
                                    depthEffect = true,
                                    chromaticAberration = chromaticAberration > 0f
                                )
                            },
                            highlight = { Highlight.Plain },
                            layerBlock = {
                                val offset = offsetAnimation.value
                                val zoom = zoomAnimation.value
                                val rotation = rotationAnimation.value
                                translationX = offset.x
                                translationY = offset.y
                                scaleX = zoom
                                scaleY = zoom
                                rotationZ = rotation
                            }
                        )
                        .pointerInput(animationScope) {
                            fun Offset.rotateBy(angle: Float): Offset {
                                val angleInRadians = angle * (PI / 180)
                                val cos = cos(angleInRadians)
                                val sin = sin(angleInRadians)
                                return Offset(
                                    (x * cos - y * sin).toFloat(),
                                    (x * sin + y * cos).toFloat()
                                )
                            }

                            detectTransformGestures { _, pan, gestureZoom, gestureRotate ->
                                val offset = offsetAnimation.value
                                val zoom = zoomAnimation.value
                                val rotation = rotationAnimation.value

                                val targetZoom = (zoom * gestureZoom).coerceIn(0.5f, 3f)
                                val targetRotation = rotation + gestureRotate
                                val targetOffset = offset + pan.rotateBy(targetRotation) * targetZoom

                                animationScope.launch {
                                    offsetAnimation.snapTo(targetOffset)
                                    zoomAnimation.snapTo(targetZoom)
                                    rotationAnimation.snapTo(targetRotation)
                                }
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Interactive Glass",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Drag, Pinch, or Rotate",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassButton(
                        onClick = onBackClick
                    ) {
                        Text("Back", color = Color.White, fontWeight = FontWeight.Medium)
                    }

                    GlassButton(
                        onClick = {
                            animationScope.launch {
                                offsetAnimation.animateTo(Offset.Zero)
                                zoomAnimation.animateTo(1f)
                                rotationAnimation.animateTo(0f)
                            }
                        }
                    ) {
                        Text("Reset Transform", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }

                // Bottom Controls Panel
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    GlassButton(
                        onClick = { isSheetExpanded = !isSheetExpanded },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = if (isSheetExpanded) "🔽 Hide Controls" else "🔼 Show Controls",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    AnimatedVisibility(
                        visible = isSheetExpanded,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut()
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Glass Refraction Playground",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Column {
                                    Text(
                                        text = "Corner Radius: ${(cornerRadiusFrac * 100).toInt()}%",
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Slider(
                                        value = cornerRadiusFrac,
                                        onValueChange = { cornerRadiusFrac = it },
                                        valueRange = 0f..1f
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Blur Radius: ${blurRadiusDp.toInt()} dp",
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Slider(
                                        value = blurRadiusDp,
                                        onValueChange = { blurRadiusDp = it },
                                        valueRange = 0f..32f
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Refraction Height: ${(refractionHeightFrac * 100).toInt()}%",
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Slider(
                                        value = refractionHeightFrac,
                                        onValueChange = { refractionHeightFrac = it },
                                        valueRange = 0f..1f
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Refraction Displacement: ${(refractionAmountFrac * 100).toInt()}%",
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Slider(
                                        value = refractionAmountFrac,
                                        onValueChange = { refractionAmountFrac = it },
                                        valueRange = 0f..1f
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Chromatic Dispersion: ${if (chromaticAberration > 0f) "Enabled" else "Disabled"}",
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Slider(
                                        value = chromaticAberration,
                                        onValueChange = { chromaticAberration = it },
                                        valueRange = 0f..1f
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
