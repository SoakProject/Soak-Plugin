plugins {
    id("java-library")
}

group = "org.soak.wrapper.api"

dependencies {
    api("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    api("net.md-5:bungeecord-chat:1.16-R0.4")
    implementation("org.spongepowered:spongeapi:8.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.WARN
    val fat = configurations.compileClasspath.get()
            .filter {
                val name = it.name;
                if (name.startsWith("paper-api")) {
                    return@filter true
                }
                if (name.startsWith("commons-lang")) {
                    return@filter true
                }
                if (name.startsWith("bungeecord-chat")) {
                    return@filter true
                }
                return@filter false
            }
            .map {
                return@map zipTree(it)
            }
    from(fat).exclude {
        return@exclude it.name.equals("JavaPluginLoader.class") || it.name.equals("Material.class") || it.name.equals("EntityType.class")
    }
}

tasks.test {
    useJUnitPlatform()
}