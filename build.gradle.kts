plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
}

allprojects {
    group = "es.edwardbelt"
    version = "1.1.0"

    repositories {
        mavenCentral()
        maven("https://repo.spongepowered.org/maven")
        maven("https://maven.hytale.com/release")
    }
}