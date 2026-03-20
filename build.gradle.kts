plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
}

allprojects {
    group = "es.edwardbelt"
    version = "1.1.1"

    repositories {
        mavenCentral()
        maven("https://repo.spongepowered.org/maven")
        maven("https://maven.hytale.com/release")
    }

    plugins.withId("java") {
        tasks.named<ProcessResources>("processResources") {
            val projectVersion = version
            filesMatching("manifest.json") {
                expand("version" to projectVersion)
            }
        }
    }
}