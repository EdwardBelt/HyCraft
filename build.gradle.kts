plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
}

project.group = "es.edwardbelt"
project.version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}


val LOCAL_SERVER_JAR = true

fun getHytaleServerJarLocal(): String {
    val localDir = file("lib")
    if (!localDir.exists()) localDir.mkdirs()

    val hytaleSeverJar = File(localDir, "HytaleServer.jar")
    if (!hytaleSeverJar.exists()) error("Please put a valid HytaleServer.jar in ${localDir.absolutePath}")

    return hytaleSeverJar.absolutePath
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.42")
    compileOnly(files(getHytaleServerJarLocal()))
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("net.kyori:adventure-nbt:4.26.1") // TODO: remove
}