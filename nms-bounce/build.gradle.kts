plugins {
    id("java-library")
}

group = "org.soak.bounce"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":nms-replicate"))
    implementation("org.spongepowered:spongeapi:8.1.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}