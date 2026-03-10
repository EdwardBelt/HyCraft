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

    val getHytaleServerJar = rootProject.ext["getHytaleServerJar"] as () -> String
    compileOnly(files(getHytaleServerJar()))

    implementation("net.bytebuddy:byte-buddy:1.18.7")
    implementation("net.bytebuddy:byte-buddy-agent:1.18.7")
}