plugins {
    `java-library`
    id("com.gradleup.shadow")
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
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
    mergeServiceFiles()
}

dependencies {
    implementation(project(":api"))

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    compileOnly("com.hypixel.hytale:Server:2026.02.19-1a311a592")

    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras)
    annotationProcessor(libs.mixin)
    annotationProcessor(libs.mixinextras)
}