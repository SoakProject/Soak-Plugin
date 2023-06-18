plugins {
    id("java-library")
}

group = "org.soak"

allprojects {
    version = "1.0.0"
    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}