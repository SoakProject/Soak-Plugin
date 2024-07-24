plugins {
    id("java-library")
}

group = "org.soak.wrapper.vanilla"
version = "1.0.0"

java {
    toolchain {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(project(":bukkit-api"))
    implementation(project(":Common"))
    implementation("org.spongepowered:spongeapi:10.0.0")
    implementation(project(mapOf("path" to ":Wrapper")))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}