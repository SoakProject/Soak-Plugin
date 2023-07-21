plugins {
    id("java")
}

group = "org"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":bukkit-api"))
    implementation("org.spongepowered:spongeapi:8.0.0")
    implementation(project(mapOf("path" to ":plugin")))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}