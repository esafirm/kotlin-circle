plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("maven-publish")
    id("nolambda.circle.publish")
}

circle {
    artifactId.set("circle")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.1"
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.ui)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                api(compose.components.resources)

                // Image loader
                api("io.github.qdsfdhvh:image-loader:1.2.10")

                val ktorVersion = "2.2.4"
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                api("io.ktor:ktor-client-auth:$ktorVersion")
                api("io.ktor:ktor-client-logging:$ktorVersion")

                api("cafe.adriel.voyager:voyager-navigator:1.0.0-rc05")

                // Workaround as per https://youtrack.jetbrains.com/issue/KT-41821
                implementation("org.jetbrains.kotlinx:atomicfu:0.18.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "nolambda.stream.circle"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}
