plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("8.0.2").apply(false)
    kotlin("multiplatform").version("1.8.20").apply(false)
    id("org.jetbrains.compose").version("1.4.0").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Group and version that will be used in the generated POM file
group = "io.github.esafirm.circle"
version = "0.1.0"