# Consumer ProGuard rules shipped inside the GlassKit AAR
# Automatically applied when client applications build with R8/ProGuard

-keep public class io.github.ajayvamsee.glasskit.** {
    public protected <methods>;
    public protected <fields>;
}

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
