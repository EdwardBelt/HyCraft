import java.io.File

plugins {
    `java-library`
    id("com.gradleup.shadow")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.processResources {
    val projectVersion = version
    filesMatching("manifest.json") {
        expand("version" to projectVersion)
    }
}

tasks.shadowJar {
    archiveBaseName.set("HyCraft")
    archiveVersion.set(version.toString())
    archiveClassifier.set("")
}

fun getHytaleServerJarLocal(): String {
    val localDir = file("../lib")
    if (!localDir.exists()) localDir.mkdirs()

    val hytaleServerJar = File(localDir, "HytaleServer.jar")
    if (!hytaleServerJar.exists()) {
        error("Please put a valid HytaleServer.jar in ${localDir.absolutePath}")
    }

    return hytaleServerJar.absolutePath
}

dependencies {
    implementation(project(":api"))

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    compileOnly(files(getHytaleServerJarLocal()))
}