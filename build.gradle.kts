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

            // Usar ktlint con versión específica
            ktlint("1.1.1")
        }

        kotlinGradle {
            target("**/*.kts")
            ktlint()
        }
    }
}
