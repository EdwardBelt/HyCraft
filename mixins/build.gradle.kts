plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.jar {
    archiveBaseName.set("HyCraft-Mixins")
}

dependencies {
    implementation("com.hypixel.hytale:Server:2026.02.19-1a311a592")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    compileOnly("org.spongepowered:mixin:0.8.7")
}