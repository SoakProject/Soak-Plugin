import org.gradle.internal.impldep.com.google.common.io.Files
import java.io.FileOutputStream
import java.io.FileWriter

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

    var worldeditPath = File(project.projectDir, "libs").listFiles()?.filter {
        logger.info("file: " + it.path);
        return@filter it.name.toLowerCase().contains("worldedit")
    }?.firstOrNull();

    if (worldeditPath == null) {
        logger.warn("No local worldedit sponge plugin found in '" + project.projectDir + "/libs/worldedit.jar'. Downloading now")
        val worldeditStream = uri("https://ore.spongepowered.org/EngineHub/WorldEdit/versions/7.2.16+6534-2066eb4/download").toURL().openStream();
        val file = File(project.projectDir, "libs/worldedit.jar");
        file.parentFile.mkdirs();
        file.createNewFile();
        val writer = FileOutputStream(file)
        worldeditStream.copyTo(writer);
        worldeditPath = file;
    }
    implementation(files(worldeditPath))

}