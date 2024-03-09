plugins {
    id("java-library")
}

group = "org.soak.plugin"

java {
    toolchain {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.sk89q.worldedit:worldedit-core:7.2.14")
    //this doesnt work ... use local libs
    implementation("com.sk89q.worldedit:worldedit-sponge:7.2.14")
    implementation(project(mapOf("path" to ":plugin")))
    implementation("org.spongepowered:spongeapi:10.0.0")

    val worldeditPath = File(project.projectDir,"libs").listFiles()?.filter {
        logger.info("file: " + it.path);
        return@filter it.name.toLowerCase().contains("worldedit")
    }?.firstOrNull();

    if (worldeditPath == null) {
        logger.warn("No local worldedit sponge plugin found in '" + project.projectDir + "/libs/worldedit.jar'. Here's hoping the maven is fixed")
    } else {
        implementation(files(worldeditPath))
    }

}