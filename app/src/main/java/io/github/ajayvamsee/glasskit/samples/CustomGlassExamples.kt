package io.github.ajayvamsee.glasskit.samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ajayvamsee.glasskit.components.GlassButton
import io.github.ajayvamsee.glasskit.components.GlassCard
import io.github.ajayvamsee.glasskit.components.GlassContainer
import io.github.ajayvamsee.glasskit.components.GlassSurface
import io.github.ajayvamsee.glasskit.effects.blur
import io.github.ajayvamsee.glasskit.effects.lens
import io.github.ajayvamsee.glasskit.effects.vibrancy
import io.github.ajayvamsee.glasskit.highlight.Highlight
import io.github.ajayvamsee.glasskit.internal.requireGlassBackdrop
import io.github.ajayvamsee.glasskit.modifiers.drawBackdrop
import io.github.ajayvamsee.glasskit.theme.GlassColors
import io.github.ajayvamsee.glasskit.theme.GlassRefraction
import io.github.ajayvamsee.glasskit.theme.GlassShapes
import io.github.ajayvamsee.glasskit.theme.GlassTheme

/**
 * SAMPLE 1: Global Custom Theme Setup
 * Demonstrates configuring theme colors, default blur ratio, component shapes,
 * and refraction settings globally via [GlassTheme].
 */
@Composable
fun CustomGlassThemeSample() {
    GlassTheme(
        darkTheme = true,
        colors = GlassColors(
            surfaceTint = Color.White.copy(alpha = 0.15f),
            backgroundOverlay = Color.Black.copy(alpha = 0.20f),
            accent = Color(0xFF00E5FF),
            borderColor = Color.White.copy(alpha = 0.40f)
        ),
        blurRadius = 24.dp,
        shapes = GlassShapes(
            cardShape = RoundedCornerShape(32.dp),
            surfaceShape = RoundedCornerShape(20.dp),
            buttonShape = RoundedCornerShape(16.dp),
            cornerRadius = 24.dp
        ),
        refraction = GlassRefraction(
            depth = 16.dp,
            chromaticAberration = true,
            highlightAlpha = 0.8f
        )
    ) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF3B0764), Color(0xFF0284C7))
                            )
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Card with Theme Shapes & Blur", Modifier.padding(20.dp), color = Color.White)
                }

                GlassButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Theme Accent Button")
                }
            }
        }
    }
}

/**
 * SAMPLE 2: Per-Component Custom Parameter Overrides
 * Demonstrates overriding specific parameters (shape, blurRadius, tint, refraction)
 * on individual components.
 */
@Composable
fun CustomComponentParametersSample() {
    GlassTheme(darkTheme = true) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0284C7), Color(0xFF581C87), Color(0xFF0F172A))
                            )
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    blurRadius = 12.dp,
                    tint = Color.Cyan.copy(alpha = 0.20f)
                ) {
                    Text("Custom Parameters GlassCard", Modifier.padding(20.dp), color = Color.White)
                }

                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                    blurRadius = 35.dp,
                    tint = Color.Magenta.copy(alpha = 0.15f)
                ) {
                    Text("Custom Surface (35dp Blur)", Modifier.padding(20.dp), color = Color.White)
                }

                GlassButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    tint = Color(0xFFFF4081)
                ) {
                    Text("Custom Pink Accent Button")
                }
            }
        }
    }
}

/**
 * SAMPLE 3: Low-Level Custom Refraction & Shader Effects
 * Demonstrates applying custom refraction depth, dispersion, and blur shaders directly
 * using [drawBackdrop].
 */
@Composable
fun CustomRefractionShaderSample() {
    GlassTheme(darkTheme = true) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF7C3AED), Color(0xFF0284C7))
                            )
                        )
                )
            }
        ) {
            val backdrop = requireGlassBackdrop()

            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(24.dp) },
                        effects = {
                            vibrancy()
                            blur(20.dp.toPx())
                            lens(
                                refractionHeight = 16.dp.toPx(),
                                refractionAmount = 32.dp.toPx(),
                                chromaticAberration = true
                            )
                        },
                        highlight = { Highlight.Plain }
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Low-Level Custom Lens & Refraction Shader",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Screen displaying code samples for customization options.
 */
@Composable
fun CustomExamplesScreen(
    onBackClick: () -> Unit
) {
    GlassTheme(darkTheme = true) {
        GlassContainer(
            modifier = Modifier.fillMaxSize(),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF0284C7))
                            )
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 40.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    GlassButton(onClick = onBackClick) {
                        Text("Back to Showcase", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Customization Samples",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "See how to pass custom colors, blur radiuses, corner shapes, and refraction settings as parameters.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Text("1. Custom Theme Parameters", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                CustomGlassThemeSampleBox()

                Text("2. Per-Component Parameter Overrides", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                CustomComponentParametersSampleBox()
            }
        }
    }
}

@Composable
private fun CustomGlassThemeSampleBox() {
    GlassTheme(
        colors = GlassColors(
            surfaceTint = Color.Cyan.copy(alpha = 0.15f),
            backgroundOverlay = Color.Black.copy(alpha = 0.2f),
            accent = Color(0xFF00E5FF),
            borderColor = Color.Cyan.copy(alpha = 0.4f)
        ),
        blurRadius = 24.dp,
        shapes = GlassShapes(
            cardShape = RoundedCornerShape(28.dp),
            buttonShape = RoundedCornerShape(16.dp)
        ),
        refraction = GlassRefraction(depth = 16.dp, chromaticAberration = true)
    ) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("GlassCard (inherits cyan tint, 28dp shape & 24dp blur)", color = Color.White)
                GlassButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("GlassButton (inherits 0xFF00E5FF accent & 16dp shape)")
                }
            }
        }
    }
}

@Composable
private fun CustomComponentParametersSampleBox() {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        blurRadius = 10.dp,
        tint = Color.Magenta.copy(alpha = 0.18f)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("GlassCard override (shape=12dp, blur=10dp, tint=Magenta)", color = Color.White)
            GlassButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                tint = Color(0xFFFF4081)
            ) {
                Text("GlassButton override (shape=24dp, tint=Pink)")
            }
        }
    }
}
