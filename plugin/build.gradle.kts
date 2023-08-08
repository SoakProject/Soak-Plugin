plugins {
    id("java-library")
}

group = "org.soak.plugin"

dependencies {
    api(project(":bukkit-api"))
    implementation("org.spongepowered:spongeapi:8.0.0")
    implementation("org.spongepowered:plugin-spi:0.3.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    dependsOn(":bukkit-api:jar")
    dependsOn(":VanillaMaterials:jar");
    val fat = configurations.runtimeClasspath.get().filter {
        System.out.println("Build: " + it.name)
        return@filter it.name.startsWith("bukkit-api");
    }.map {
        return@map zipTree(it)
    }.toMutableList()

    val file = project.file("../VanillaMaterials/build/libs/VanillaMaterials-1.0.0.jar");
    fat.add(zipTree(file));

    from(fat)
}

tasks.test {
    useJUnitPlatform()
}