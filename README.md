# Death Muffler Fix

一个用于修复 **Mob Grinding Utils** 消声器（Death Muffler）Boss 血条隐藏失效问题的 Minecraft 模组，使用 **Stonecutter** 实现单仓库多版本分类（架构参考 [MoreFluxStorage](https://github.com/Circulate233/MoreFluxStorage)）。

## 问题背景

Mob Grinding Utils 的消声器（Death Muffler）Boss 血条隐藏功能在各版本存在不同问题：

- **1.21.1（MUG 1.1.10）**：`BossBarHidingEvent` 类构建缺失（jar 中只有 .java 没有 .class），客户端初始化即崩溃。本模组将缺失类以同包同名 shim 的形式编译进自身 jar 补齐字节码，MGU 的 `doClientStuff()` 完整执行（血条隐藏、worldUnload、XP 流体渲染层、颜色处理器全部按原逻辑生效）。
- **1.20.1（MUG 1.1.0）**：原生功能可用，但按硬编码英文 Boss 名匹配，非英文环境失效。本模组提供语言系统名称匹配的增强实现。
- **1.12.x（MUG 0.3.13）**：原生功能可用，但同样按硬编码英文 Boss 名匹配，非英文环境失效。本模组提供语言系统名称匹配的增强实现（与原版监听叠加）。

## 功能特性

- 修复消声器的 **凋灵（Wither）** Boss 血条隐藏功能
- 修复消声器的 **末影龙（Ender Dragon）** Boss 血条隐藏功能
- 使用 Minecraft 内置语言系统进行 Boss 名称匹配，兼容多语言环境

## 版本分类架构

```
├── build.gradle.kts          # Stonecutter 中央脚本（按 platform 属性分发）
├── stonecutter.gradle.kts    # 当前激活版本（stonecutter.active）
├── settings.gradle.kts       # Stonecutter 版本声明（1.12.2 / 1.20.1 / 1.21.1）
├── gradle/scripts/           # 平台配置脚本
│   ├── platform-rfg.gradle          # 1.12.2  RetroFuturaGradle
│   ├── platform-legacyforge.gradle  # 1.20.1  NeoForge ModDev LegacyForge
│   └── platform-neoforge.gradle     # 1.21.1  NeoForge ModDev
├── src/main/resources/       # 共享资源（logo.png、lang 等，Stonecutter 自动合并）
├── libs/                     # 各版本 MUG jar（flatDir 本地依赖）
└── versions/
    ├── 1.12.2/               # platform=rfg，Java 21 编译 / Java 8 目标（构建工具链 1.12.2，产物兼容整个 1.12.x）
    ├── 1.20.1/               # platform=legacyforge，Java 17
    └── 1.21.1/               # platform=neoforge，Java 21
```

## 依赖要求

| 版本 | 加载器 | MUG | Java |
|------|--------|-----|------|
| 1.21.1 | NeoForge 21.1.230+ | 1.1.10+ | 21 |
| 1.20.1 | Forge 47.1.106+ | 1.1.0+ | 17 |
| 1.12.x（1.12/1.12.1/1.12.2） | Forge 14.21+ | 0.3.13+ | 8（运行）/ 21（构建） |

## 构建

```bash
# 切换版本（默认激活 1.21.1）
./gradlew stonecutterSwitchTo1.21.1   # 或 stonecutterSwitchTo1.20.1 / stonecutterSwitchTo1.12.2

# 构建当前激活版本
./gradlew build
```

构建产物位于 `versions/<mc>/build/libs/` 目录，命名统一为 `<modid>-<version>-<Loader>-<MC版本>.jar`（如 `death_muffler_fix-1.0.0-NeoForge-1.21.1.jar`，1.12.2 的 dev 版追加 `-dev` 后缀）。1.12.2 产物（`death_muffler_fix-1.0.0-Forge-1.12.2.jar`）同样适用于 1.12 / 1.12.1。

> 各版本 MUG jar（`mob_grinding_utils-0.3.13/1.1.0/1.1.10.jar`）不在仓库中，需从 [CurseForge - Mob Grinding Utils](https://www.curseforge.com/minecraft/mc-mods/mob-grinding-utils) 下载对应版本后放入根目录 `libs/` 文件夹。

> 注意：1.12.2 使用 RetroFuturaGradle，需要 JDK 8（Minecraft 运行目标）与 JDK 21/25（构建工具链）。本地 JDK 路径在根 `gradle.properties` 的 `org.gradle.java.installations.paths` 中配置。

## 安装

将构建生成的 `.jar` 文件放入对应版本 Minecraft 的 `mods` 文件夹即可。

## 许可证

本项目采用 [GPL-3.0 License](LICENSE) 开源。

## 作者

- **Barbatosishere**
