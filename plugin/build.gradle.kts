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

dependencies {
    var forgeSpis = fileTree(gradle.gradleUserHomeDir.absolutePath + "/caches")
        .files
        .filter {
            return@filter it.name.endsWith(".jar");
        }
        .filter {
            return@filter it.name.startsWith("forgespi");
        }
    var forgeFmls = fileTree(gradle.gradleUserHomeDir.absolutePath + "/caches")
        .files
        .filter {
            return@filter it.name.endsWith("universal.jar");
        }
        .filter {
            return@filter it.name.startsWith("forge-");
        }

    var forgeEventBuses = fileTree(gradle.gradleUserHomeDir.absolutePath + "/caches")
        .files
        .filter {
            return@filter it.name.endsWith(".jar");
        }
        .filter {
            return@filter it.name.startsWith("eventbus");
        }
    if (forgeSpis.isEmpty() || forgeFmls.isEmpty() || forgeEventBuses.isEmpty()) {
        throw RuntimeException("Missing ForgeSPI/ForgeFML/EventBus from gradle cache -> Build a forge mod in another project to gain these");
    }

    api(project(":bukkit-api"))
    api(project(":nms-bounce"))
    implementation(files(forgeSpis.iterator().next()))
    implementation(files(forgeFmls.iterator().next()))
    implementation(files(forgeEventBuses.iterator().next()))
    implementation(project(":nms-replicate"))
    implementation("org.spongepowered:spongeapi:10.0.0")
    implementation("org.spongepowered:plugin-spi:0.3.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    dependsOn(":bukkit-api:jar")
    dependsOn(":nms-bounce:jar")
    dependsOn(":VanillaMaterials:jar");
    val fat = configurations.runtimeClasspath.get().filter {
        return@filter it.name.startsWith("bukkit-api") || it.name.startsWith("nms-bounce");
    }.map {
        return@map zipTree(it)
    }.toMutableList()

    val materialFile = project.file("../VanillaMaterials/build/libs/VanillaMaterials-1.0.0.jar");
    fat.add(zipTree(materialFile));

    from(fat).exclude {
        return@exclude it.name == "Compatibility.class" && !it.file.absolutePath.contains("zip_");
    }
}

tasks.test {
    useJUnitPlatform()
}