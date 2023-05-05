plugins {
    kotlin("jvm").version("1.6.10")
    id("com.github.johnrengelman.shadow") version "4.0.4"
    java
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT")
}