package io.github.ajayvamsee.glasskit

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import org.intellij.lang.annotations.Language

/**
 * Cache for runtime shaders used by GlassKit drawing effects.
 *
 * This lower-level API is primarily consumed by custom
 * [io.github.ajayvamsee.glasskit.highlight.HighlightStyle] implementations.
 */
sealed interface RuntimeShaderCache {

    /**
     * Returns a cached shader for [key], compiling [string] only when needed.
     *
     * @param key stable cache key for the shader source.
     * @param string AGSL source compiled into the shader.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun obtainRuntimeShader(
        key: String,
        @Language("AGSL") string: String
    ): RuntimeShader
}

internal class RuntimeShaderCacheImpl : RuntimeShaderCache {

    private val runtimeShaders = mutableMapOf<String, RuntimeShader>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun obtainRuntimeShader(key: String, string: String): RuntimeShader {
        return runtimeShaders.getOrPut(key) { RuntimeShader(string) }
    }

    fun clear() {
        runtimeShaders.clear()
    }
}
