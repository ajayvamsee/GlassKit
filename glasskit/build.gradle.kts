plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

android {
    namespace = "io.github.ajayvamsee.glasskit"
    compileSdk {
        version = release(36)
    }
    buildToolsVersion = "36.1.0"

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    moduleName.set("GlassKit")
    dokkaSourceSets.configureEach {
        sourceRoots.from(file("src/main/java"))
    }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kyant.shapes)
}

val hasSigningKey = providers.gradleProperty("signingInMemoryKey").isPresent

mavenPublishing {
    publishToMavenCentral()
    if (hasSigningKey) {
        signAllPublications()
    }
    coordinates("io.github.ajayvamsee", "glasskit", "1.0.0")

    pom {
        name.set("GlassKit")
        description.set("Reusable Jetpack Compose liquid glass and backdrop blur components.")
        url.set("https://github.com/ajayvamsee/GlassKit")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("ajayvamsee")
                name.set("Ajay Vamsee")
            }
        }
        scm {
            url.set("https://github.com/ajayvamsee/GlassKit")
            connection.set("scm:git:git://github.com/ajayvamsee/GlassKit.git")
            developerConnection.set("scm:git:ssh://git@github.com/ajayvamsee/GlassKit.git")
        }
    }
}

tasks.configureEach {
    if (name.startsWith("publish") && name.contains("MavenCentral")) {
        doFirst {
            check(hasSigningKey) {
                "Maven Central publication requires the Gradle property signingInMemoryKey."
            }
        }
    }
}
