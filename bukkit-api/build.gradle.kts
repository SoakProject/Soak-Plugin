plugins {
    id("java-library")
}

group = "org.soak.wrapper.api"

dependencies {
    api("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.WARN
    val fat = configurations.compileClasspath.get()
            .filter {
                if (it.name.startsWith("paper-api")) {
                    return@filter true
                }
                if (it.name.startsWith("commons-lang")) {
                    return@filter true
                }
                return@filter false
            }
            .map {
                return@map zipTree(it)
            }
    from(fat).exclude {
        return@exclude it.name.equals("JavaPluginLoader.class")
    }
}

tasks.test {
    useJUnitPlatform()
}