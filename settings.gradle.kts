pluginManagement {
    repositories {
        // KikuGie：Stonecutter 插件
        maven("https://maven.kikugie.dev/releases") {
            name = "KikuGie Releases"
        }
        maven("https://maven.kikugie.dev/snapshots") {
            name = "KikuGie Snapshots"
        }
        maven("https://maven.minecraftforge.net/") {
            name = "MinecraftForge Plugins"
        }
        // NeoForged 镜像优先（国内可直连，参考 Compact Extreme Reactor）
        maven("https://neoforged.forgecdn.net/releases") {
            name = "NeoForged Mirror"
        }
        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged Plugins"
        }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.8.3"
}

rootProject.name = rootDir.name

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(getRootProject()) {
        versions("1.20.1", "1.21.1")
        vcsVersion = "1.20.1"
    }
}
