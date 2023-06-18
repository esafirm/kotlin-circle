package nolambda.circle.gradle.convention

import org.gradle.api.model.ObjectFactory

/**
 * Extension for [org.gradle.api.Project] to provide information about the module.
 */
abstract class CircleExt(factory: ObjectFactory) {

    val artifactId = factory.property(String::class.java)
    companion object {

        private const val EXT_NAME = "circle"

        fun getOrRegister(project: org.gradle.api.Project): CircleExt {
            return project.extensions.findByType(CircleExt::class.java)
                ?: project.extensions.create(EXT_NAME, CircleExt::class.java)
        }
    }
}