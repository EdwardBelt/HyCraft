plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
}

ext {
    set("getHytaleServerJar", fun(): String {
        val localDir = file("lib")
        if (!localDir.exists()) localDir.mkdirs()

        val hytaleServerJar = File(localDir, "HytaleServer.jar")
        if (!hytaleServerJar.exists()) {
            error("Please put a valid HytaleServer.jar in ${localDir.absolutePath}")
        }

        return hytaleServerJar.absolutePath
    })
}

allprojects {
    group = "es.edwardbelt"
    version = "1.0.0"
}