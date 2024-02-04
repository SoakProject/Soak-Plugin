plugins {
    id("java-library")
}

group = "org.soak.wrapper.api"

java {
    toolchain {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("org.spongepowered:spongeapi:10.0.0")
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
        return@exclude it.name.equals("JavaPluginLoader.class") ||
                it.name.equals("Material.class") ||
                it.name.equals("EntityType.class") ||
                it.name.equals("InventoryType.class") ||
                it.name.equals("InventoryType\$SlotType.class") ||
                it.name.equals("GameMode.class") ||
                it.name.equals("PaperPluginLogger.class")
    }
}

tasks.test {
    useJUnitPlatform()
}