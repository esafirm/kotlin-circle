import nolambda.circle.gradle.convention.CircleExt
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.Base64
import java.util.Properties

plugins {
    `maven-publish`
    signing
}

private val propToEnvVarList = listOf(
    "circle.enablePublishing" to "ENABLE_PUBLISHING",
    "circle.releasePublishing" to "PUBLISH_RELEASE",
    "signing.password" to "SIGNING_PASSWORD",
    "signing.key" to "SIGNING_KEY",
    "ossrhUsername" to "OSSRH_USERNAME",
    "ossrhPassword" to "OSSRH_PASSWORD"
)

val secretPropsFile: File = project.rootProject.file("local.properties")
val localProperties = Properties().apply {
    if (secretPropsFile.exists()) {
        secretPropsFile.reader().use { load(it) }
    }
}

// Load from local.properties or environment variables
propToEnvVarList.forEach {
    extra[it.first] = localProperties[it.first] ?: System.getenv(it.second)
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("source")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

val circleExt = CircleExt.getOrRegister(project)

fun PublishingExtension.setupSigning() {
    val base64encodedKey = extra["signing.key"]?.toString()
        ?: error("Signing key is not set. Please check your environment again")

    val signingKey = String(Base64.getDecoder().decode(base64encodedKey))

    signing {
        useInMemoryPgpKeys(
            signingKey,
            extra["signing.password"] as String
        )
        sign(publishing.publications)
    }
}

fun PublishingExtension.setupMavenRepositories() {
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = extra["ossrhUsername"]?.toString() ?: error("ossrh username is not set")
                password = extra["ossrhPassword"]?.toString() ?: error("ossrh password is not set")
            }
        }
    }
}

fun Project.setupPublishing() {

    publishing {

        val isLocalPublishing = extra["circle.releasePublishing"] as? String == "true"
        if (isLocalPublishing) {
            setupSigning()
            setupMavenRepositories()
        }

        // Configure all publications
        publications.withType<MavenPublication> {

            val targetArtifactId = circleExt.artifactId.getOrElse(project.name)

            println("Group ID: ${rootProject.group}")
            println("Root project name: ${rootProject.name}")
            println("Artifact ID: $targetArtifactId")

            // Configure the publication
            artifactId = targetArtifactId
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()

            // Stub javadoc.jar artifact needed by sonatype closing process
            artifact(javadocJar.get())

            // Stub source.jar artifact
            artifact(sourceJar.get())

            // Provide artifacts information requited by Maven Central
            pom {
                name.set("Compose UI Book")
                description.set("UI Component explorer for Jetpack Compose")
                url.set("https://github.com/esafirm/compose-ui-book")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("esafirm")
                        name.set("Esa Firman")
                        email.set("esafirm21@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/esafirm/compose-ui-book")
                }

            }
        }
    }
}

val enablePublishing = extra["circle.enablePublishing"] as? String == "true"
if (enablePublishing.not()) {
    logger.lifecycle(
        """
        Publishing is disabled.

        To enable it, set the environment variable ENABLE_PUBLISHING to true.
        Or set the property `circle.enablePublishing` to true in local.properties

    """.trimIndent()
    )
} else {
    afterEvaluate { setupPublishing() }
}