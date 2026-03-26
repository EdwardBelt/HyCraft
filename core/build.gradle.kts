plugins {
    `java-library`
    id("com.gradleup.shadow")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
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
    compileOnly(project(":mixins"))

    implementation("com.hypixel.hytale:Server:${property("hytaleVersion")}")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}