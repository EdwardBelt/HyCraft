plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
}

allprojects {
    group = "es.edwardbelt"
    version = "1.1.0"

    repositories {
        mavenCentral()
        maven {
            name = "hytale-release"
            url = uri("https://maven.hytale.com/release")
        }
        maven {
            name = "hytale-pre-release"
            url = uri("https://maven.hytale.com/pre-release")
        }
    }
}