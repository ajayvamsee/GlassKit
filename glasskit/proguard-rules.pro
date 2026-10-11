# ------------------------------------------------------------------
# GlassKit ProGuard / R8 Obfuscation & Shrinking Rules
# ------------------------------------------------------------------

# Enable class repackaging and access modification for maximum obfuscation
-repackageclasses ''
-allowaccessmodification

# Rename source file attributes to hide original class filenames
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,EnclosingMethod,InnerClasses

# ------------------------------------------------------------------
# Preserve Public Library API Surface
# ------------------------------------------------------------------

-keep public class io.github.ajayvamsee.glasskit.Backdrop { public *; }
-keep public class io.github.ajayvamsee.glasskit.BackdropEffectScope { public *; }

-keep public class io.github.ajayvamsee.glasskit.components.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.theme.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.blur.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.backdrops.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.effects.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.highlight.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.shadow.** {
    public protected <methods>;
    public protected <fields>;
}

-keep public class io.github.ajayvamsee.glasskit.modifiers.** {
    public protected <methods>;
    public protected <fields>;
}

# Preserve Jetpack Compose composable metadata
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# ------------------------------------------------------------------
# Internal classes in io.github.ajayvamsee.glasskit.internal.**
# and io.github.ajayvamsee.glasskit.animation.** are NOT kept
# and will be fully obfuscated and minified in release builds.
# ------------------------------------------------------------------
