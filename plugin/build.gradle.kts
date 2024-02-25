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
        url = uri("https://maven.minecraftforge.net")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    api(project(":bukkit-api"))
    api(project(":nms-bounce"))
    implementation(project(":nms-replicate"))
    implementation("com.github.mosemister:MoseStream:master-SNAPSHOT")

    implementation("net.minecraftforge:fmlcore:1.19.4-45.2.8")
    implementation("net.minecraftforge:eventbus:6.0.3")
    implementation("net.minecraftforge:forgespi:6.0.0")
    implementation("org.spongepowered:spongeapi:10.0.0")
    implementation("org.spongepowered:plugin-spi:0.3.0")

    implementation("org.slf4j:slf4j-api")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    dependsOn(":bukkit-api:jar")
    dependsOn(":nms-bounce:jar")
    dependsOn(":VanillaMaterials:jar");
    val fat = configurations.runtimeClasspath.get().filter {
        System.out.println("Name: " + it.name);
        return@filter it.name.startsWith("bukkit-api") || it.name.startsWith("nms-bounce") || it.name.startsWith("MoseStream")
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