import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.bundling.AbstractArchiveTask

plugins {
    java
    `java-library`
    `maven-publish`
    // 各平台插件按需启用（通过 gradle/scripts/platform-*.gradle 应用）：NeoForge ModDev + LegacyForge
    id("net.neoforged.moddev") version "2.0.140" apply false
    id("net.neoforged.moddev.legacyforge") version "2.0.140" apply false
}

// 平台由 versions/<mc>/gradle.properties 的 platform 属性决定：
//   neoforge     -> 新版 NeoForge（1.21.1）
//   legacyforge  -> NeoForge ModDev LegacyForge（Forge 1.20.1）
val currentPlatform = (findProperty("platform") ?: "neoforge").toString()

// Loader 标签（用于产物命名）：neoforge -> NeoForge，forge -> Forge
val loaderLabel = when ((findProperty("mod_loader") ?: "forge").toString().lowercase()) {
    "neoforge" -> "NeoForge"
    else -> "Forge"
}

base {
    archivesName = "death_muffler_fix"
}

java {
    toolchain {
        val javaVersion = findProperty("java_version")?.toString() ?: "21"
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

// ---- 资源目录：Stonecutter 0.8 自动将根 src/main/resources 作为共享资源合并到各版本 ----
// （激活版本直接引用，非激活版本经 swap 到 build/generated/stonecutter），无需手动配置

repositories {
    maven {
        name = "BMCLAPI NeoForge"
        url = uri("https://bmclapi2.bangbang93.com/maven/neoforged/")
    }
    // 阿里云镜像（Maven Central 缓存镜像）优先，避免 mavenCentral 直连超时（国内网络）
    maven {
        name = "Aliyun"
        url = uri("https://maven.aliyun.com/repository/public")
    }
    mavenCentral()
    // NeoForged 镜像优先：maven.neoforged.net 直连 SSL 握手不稳定（国内网络必失败）
    maven {
        name = "NeoForged Mirror"
        url = uri("https://neoforged.forgecdn.net/releases")
    }
    maven {
        name = "NeoForge"
        url = uri("https://maven.neoforged.net/releases/")
    }
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    // 各版本 MUG 依赖（libs/mob_grinding_utils-*.jar）
    flatDir {
        dirs(rootProject.file("libs"))
    }
}

// ---- 平台分支配置（参考 MoreFluxStorage 的 gradle/scripts/platform-*.gradle） ----
when (currentPlatform) {
    "neoforge" -> apply(from = rootProject.file("gradle/scripts/platform-neoforge.gradle"))
    "legacyforge" -> apply(from = rootProject.file("gradle/scripts/platform-legacyforge.gradle"))
    else -> throw GradleException("Unknown platform: $currentPlatform (expected neoforge or legacyforge)")
}

// ---- 版本专属依赖：versions/<mc>/dependencies.gradle，不存在则回退根目录 ----
val projectDependenciesFile = if (file("dependencies.gradle").exists()) file("dependencies.gradle") else rootProject.file("dependencies.gradle")
apply(from = projectDependenciesFile)

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

// 统一产物命名：<modid>-<version>-<Loader>-<MC版本>.jar（classifier 如 dev 自动追加后缀）
// 用 AbstractArchiveTask 而非 Jar：LegacyForge 的 jar 任务不是标准 Jar 类型
tasks.withType<AbstractArchiveTask>().configureEach {
    val mv = (findProperty("mod_version") ?: "1.0.0").toString()
    val mc = (findProperty("minecraft_version") ?: "unknown").toString()
    archiveVersion.set("$mv-$loaderLabel-$mc")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Automatic-Module-Name" to "death_muffler_fix",
        )
    }
}
