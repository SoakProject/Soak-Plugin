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
    api("org.bstats:bstats-base:3.0.2")
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
            if (name.startsWith("bstats")) {
                return@filter true
            }
            if (name.startsWith("adventure-text-logger-slf4j")) {
                return@filter true
            }
            System.out.println("BukkitName: " + name);
            return@filter false
        }
        .map {
            return@map zipTree(it)
        }
    from(fat).exclude {
        return@exclude it.name.equals("Material.class") ||
                it.name.equals("EntityType.class") ||
                it.name.equals("InventoryType.class") ||
                it.name.equals("InventoryType\$SlotType.class") ||
                it.name.equals("GameMode.class") ||
                it.name.equals("Biome.class") ||
                it.name.equals("PaperPluginLogger.class")
    }
}

tasks.test {
    useJUnitPlatform()
}