import org.gradle.api.tasks.bundling.Jar

plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
}

group = "dev.breach.cherrycore"
version = "1.0.0"
description = "CherryGuns"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.minebench.de/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.jetbrains:annotations:24.1.0")

    implementation("com.github.cryptomorin:XSeries:13.8.0")
    implementation("de.tr7zw:item-nbt-api:2.15.7")
    implementation("org.codemc.worldguardwrapper:worldguardwrapper:1.2.1-SNAPSHOT")
    implementation("de.themoep:inventorygui:1.6.5")
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filesMatching(listOf("plugin.yml", "menu.yml")) {
            expand(props)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("CherryGuns.jar")

        relocate("com.cryptomorin.xseries", "dev.breach.cherryCore.CherryGuns.libs.xseries")
        relocate("de.tr7zw.changeme.nbtapi", "dev.breach.cherryCore.CherryGuns.libs.nbtapi")
        relocate("org.codemc.worldguardwrapper", "dev.breach.cherryCore.CherryGuns.libs.worldguardwrapper")
        relocate("de.themoep.inventorygui", "dev.breach.cherryCore.CherryGuns.libs.inventorygui")

        mergeServiceFiles()
        minimize {
            exclude(dependency("com.github.cryptomorin:XSeries:.*"))
            exclude(dependency("de.tr7zw:item-nbt-api:.*"))
            exclude(dependency("de.themoep:inventorygui:.*"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}