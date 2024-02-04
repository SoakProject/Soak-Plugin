plugins {
    id("java")
}

group = "org"
version = "1.0.0"

java {
    toolchain {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":bukkit-api"))
    implementation("org.spongepowered:spongeapi:10.0.0")
    implementation(project(mapOf("path" to ":plugin")))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}