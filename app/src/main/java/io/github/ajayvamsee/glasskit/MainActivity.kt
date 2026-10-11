package io.github.ajayvamsee.glasskit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ajayvamsee.glasskit.components.GlassButton
import io.github.ajayvamsee.glasskit.components.GlassCard
import io.github.ajayvamsee.glasskit.components.GlassContainer
import io.github.ajayvamsee.glasskit.components.GlassSurface
import io.github.ajayvamsee.glasskit.playground.GlassPlaygroundScreen
import io.github.ajayvamsee.glasskit.samples.CustomExamplesScreen
import io.github.ajayvamsee.glasskit.theme.GlassColors
import io.github.ajayvamsee.glasskit.theme.GlassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlassKitDemoApp()
        }
    }
}

enum class Screen {
    Showcase,
    Playground,
    CustomSamples
}

@Composable
fun GlassKitDemoApp() {
    var currentScreen by remember { mutableStateOf(Screen.Showcase) }

    when (currentScreen) {
        Screen.Showcase -> {
            GlassShowcaseContent(
                onOpenPlayground = { currentScreen = Screen.Playground },
                onOpenSamples = { currentScreen = Screen.CustomSamples }
            )
        }
        Screen.Playground -> {
            GlassPlaygroundScreen(
                onBackClick = { currentScreen = Screen.Showcase }
            )
        }
        Screen.CustomSamples -> {
            CustomExamplesScreen(
                onBackClick = { currentScreen = Screen.Showcase }
            )
        }
    }
}

@Composable
fun GlassShowcaseContent(
    onOpenPlayground: () -> Unit,
    onOpenSamples: () -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(true) }
    var blurRadiusDp by remember { mutableFloatStateOf(20f) }
    var clickCount by remember { mutableIntStateOf(0) }

    // Natural Oceanic Water Theme Palette
    val waterGlassColors = if (isDarkTheme) {
        GlassColors(
            surfaceTint = Color.White.copy(alpha = 0.10f),      // Natural translucent glass tint
            backgroundOverlay = Color(0x1F0F172A),             // Soft deep slate navy overlay
            accent = Color(0xFF38BDF8),                        // Natural ocean sky-blue accent
            borderColor = Color.White.copy(alpha = 0.22f)       // Soft edge rim reflection
        )
    } else {
        GlassColors(
            surfaceTint = Color.White.copy(alpha = 0.45f),      // Natural frosted ice tint
            backgroundOverlay = Color(0x1A0284C7),             // Gentle marine water overlay
            accent = Color(0xFF0284C7),                        // Deep ocean blue accent
            borderColor = Color.White.copy(alpha = 0.60f)       // Bright ice rim highlight
        )
    }

    GlassTheme(
        darkTheme = isDarkTheme,
        colors = waterGlassColors,
        blurRadius = blurRadiusDp.dp
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "NaturalWaveAnimation")
        val animatedOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 14000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Offset"
        )

        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (isDarkTheme) {
                                    listOf(
                                        Color(0xFF0B132B), // Deep slate navy
                                        Color(0xFF1C2541), // Deep ocean water
                                        Color(0xFF0E7490), // Natural deep sea teal
                                        Color(0xFF1E293B)  // Muted slate navy
                                    )
                                } else {
                                    listOf(
                                        Color(0xFFF0F9FF), // Natural ice mist
                                        Color(0xFFE0F2FE), // Soft ocean sky
                                        Color(0xFFBAE6FD), // Clear water aqua
                                        Color(0xFF7DD3FC)  // Soft marine blue
                                    )
                                }
                            )
                        )
                ) {
                    // Soft, organic ambient water orbs
                    Box(
                        modifier = Modifier
                            .offset(x = (animatedOffset * 0.08f).dp, y = (animatedOffset * 0.12f).dp)
                            .size(260.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x3338BDF8) else Color(0x4038BDF8))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = (-animatedOffset * 0.06f).dp, y = (animatedOffset * 0.08f).dp)
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x2B0EA5E9) else Color(0x330284C7))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = (animatedOffset * 0.05f).dp, y = (-animatedOffset * 0.1f).dp)
                            .size(290.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x2606B6D4) else Color(0x260284C7))
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 40.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Liquid Glass",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Natural Oceanic Water & Frosted Glass Blur",
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
                        )
                    }
                }

                // Glass Playground Navigation Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Interactive Glass Playground",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                        )
                        Text(
                            text = "Experiment with drag gestures, zoom scaling, 2D rotation, corner radius, blur, and chromatic refraction in real-time.",
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
                        )
                        GlassButton(
                            onClick = onOpenPlayground,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Open Playground", fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // Customization Samples Navigation Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Customization Code Examples",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                        )
                        Text(
                            text = "Explore ready-to-use sample code demonstrating how to pass theme parameters, shapes, blur ratios, and refraction settings.",
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
                        )
                        GlassButton(
                            onClick = onOpenSamples,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("View Code Examples ", fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // Theme & Controls Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Theme & Customization",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isDarkTheme) "Ocean Abyss (Dark)" else "Arctic Mist (Light)",
                                fontSize = 15.sp,
                                color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                            )
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }

                        Column {
                            Text(
                                text = "Global Blur Ratio: ${blurRadiusDp.toInt()} dp",
                                fontSize = 15.sp,
                                color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Slider(
                                value = blurRadiusDp,
                                onValueChange = { blurRadiusDp = it },
                                valueRange = 0f..50f
                            )
                        }
                    }
                }

                // Live Blur Surface Demo
                GlassSurface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Natural Glass Surface",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A)
                        )
                        Text(
                            text = "This frosted surface automatically inherits the global ${blurRadiusDp.toInt()}dp blur radius and natural translucent tint from GlassTheme.",
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
                        )
                    }
                }

                // Interactive Glass Buttons Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Interactive Controls",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color.White else Color(0xFF0F172A)
                        )

                        Text(
                            text = "Clicks registered: $clickCount",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GlassButton(
                                onClick = { clickCount++ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Tap Glass",
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            GlassButton(
                                onClick = { clickCount = 0 },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Reset",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
