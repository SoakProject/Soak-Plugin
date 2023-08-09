plugins {
    id("java-library")
}

group = "org.soak.plugin"

dependencies {
    api(project(":bukkit-api"))
    api(project(":nms-bounce"))
    implementation(project(":nms-replicate"))
    implementation("org.spongepowered:spongeapi:8.1.0")
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

    from(fat)
}

tasks.test {
    useJUnitPlatform()
}