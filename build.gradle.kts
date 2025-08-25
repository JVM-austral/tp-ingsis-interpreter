plugins {
    id("com.diffplug.spotless") version "6.25.0" apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**")

            // Usar KtLint con versión específica
            ktlint("1.1.1").editorConfigOverride(
                mapOf(
                    "indent_size" to "4",
                    "disabled_rules" to "no-wildcard-imports"
                )
            )

        }

        kotlinGradle {
            target("**/*.kts")
            ktlint()
        }
    }
}

