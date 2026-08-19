# Changelog / 更新日志

All notable changes to Death Muffler Fix will be documented in this file.

---

## [1.0.1] — 2026-08-19

### English

#### Fixed

- **1.21.1 (NeoForge)**: Reverted from shim approach back to the verified Mixin approach. The shim class (`mob_grinding_utils.events.BossBarHidingEvent`) was re-introduced in v1.0.0 erroneously; NeoForge 1.21.1's JPMS module layer rejects split packages between mod jars (`ResolutionException: Modules mob_grinding_utils and death_muffler_fix export package mob_grinding_utils.events`), causing the game to crash at module resolution before any Minecraft code loads. The Mixin approach (`DoClientStuffMixin` cancelling `doClientStuff()` before `new BossBarHidingEvent()`) is the only viable fix for MUG 1.1.10's build defect. Verified by `runServer` test (startup completed, `Done (4.327s)`, no ResolutionException).

- **Build infrastructure**: Fixed dead dependency on `com.gtnewhorizons.retrofuturagradle` (GTNH Maven) — leftover from removed 1.12.2 support. The plugin declaration was still in `build.gradle.kts` with `apply false`, causing Gradle to attempt resolution on every build and timeout when GTNH Maven was unreachable.
  - Removed GTNH Maven repository from `settings.gradle.kts` pluginManagement.
  - Removed `retrofuturagradle` plugin from `build.gradle.kts` and the `rfg` platform case from the `when` block.
  - Removed `platform-rfg.gradle` script.

- **Build infrastructure**: Reordered Maven repositories so Aliyun mirror (`maven.aliyun.com/repository/public`) is checked before `mavenCentral()`. Maven Central is frequently unreachable from China; `jst-cli-bundle-2.0.6.jar` and other dependencies were failing to resolve, causing build timeouts.

#### Removed

- **1.12.x support**: Dropped entirely. Removed `versions/1.12.2/` source tree, `dependencies.gradle`, `gradle.properties`, `mcmod.info`, `pack.mcmeta`, JDK 8 path from `gradle.properties`, `descriptions/1.12.x_*.md`, `libs/mob_grinding_utils-0.3.13.jar`, and all references from `README.md`, `DESCRIPTION.md`, `CHANGELOG.md`. Stonecutter version list changed to `1.20.1 / 1.21.1` only.

#### Changed

- Updated `README.md` to reflect 1.21.1 Mixin approach (not shim), updated directory tree for 1.20.1/1.21.1 only, removed 1.12.x requirements table.
- Updated `DESCRIPTION.md` similarly, removed 1.12.x version rows.
- Updated `build.gradle.kts` comment: changed "RFG(1.12.2)" to "LegacyForge".

---

### 中文

#### 修复

- **1.21.1（NeoForge）**：从 shim 补类方案回退为已验证可行的 Mixin 方案。v1.0.0 错误地重新引入了 shim 类（`mob_grinding_utils.events.BossBarHidingEvent`），但 NeoForge 1.21.1 的 JPMS 模块层禁止模组 jar 间拆分包（`ResolutionException: Modules mob_grinding_utils and death_muffler_fix export package mob_grinding_utils.events`），导致游戏在模块解析阶段即崩溃、无法进入任何 Minecraft 代码。Mixin 方案（`DoClientStuffMixin` 在 `new BossBarHidingEvent()` 前取消 `doClientStuff()`）是 MUG 1.1.10 构建缺陷唯一可行的修复方式。已通过 `runServer` 实测验证（启动完成，`Done (4.327s)`，无 ResolutionException）。

- **构建基础设施**：修复对 `com.gtnewhorizons.retrofuturagradle`（GTNH Maven）的死依赖——1.12.2 移除后 `build.gradle.kts` 仍以 `apply false` 声明该插件，Gradle 每次构建都尝试解析它，GTNH Maven 不可达时超时崩溃。
  - 从 `settings.gradle.kts` 的 pluginManagement 移除 GTNH Maven 仓库。
  - 从 `build.gradle.kts` 移除 `retrofuturagradle` 插件声明和 `rfg` 平台分支。
  - 删除 `platform-rfg.gradle` 脚本。

- **构建基础设施**：调整 Maven 仓库顺序，将阿里云镜像（`maven.aliyun.com/repository/public`）移到 `mavenCentral()` 之前。国内网络 mavenCentral 直连频繁超时，导致 `jst-cli-bundle-2.0.6.jar` 等依赖下载失败、构建卡死。

#### 移除

- **1.12.x 支持**：完全移除。删除 `versions/1.12.2/` 源码树、`dependencies.gradle`、`gradle.properties`、`mcmod.info`、`pack.mcmeta`、`gradle.properties` 中的 JDK 8 路径、`descriptions/1.12.x_*.md`、`libs/mob_grinding_utils-0.3.13.jar`，以及 `README.md`、`DESCRIPTION.md`、`CHANGELOG.md` 中的所有引用。Stonecutter 版本列表改为仅 `1.20.1 / 1.21.1`。

#### 变更

- 更新 `README.md`：1.21.1 修复方案描述改为 Mixin（非 shim），目录树和依赖表仅保留 1.20.1/1.21.1。
- 更新 `DESCRIPTION.md`：同上，移除 1.12.x 版本行。
- 更新 `build.gradle.kts` 注释：`RFG(1.12.2)` → `LegacyForge`。

---

## [1.0.0] — 2026-08-16

### English

#### Fixed

- **1.21.1 (NeoForge)**: Completed the Mixin-based fix for the missing `BossBarHidingEvent` (only `.java` source was shipped in MUG 1.1.10, no compiled `.class`). The Mixin cancels `doClientStuff()` right before the `new BossBarHidingEvent()` instruction; boss bar hiding is reimplemented with a language-aware listener.
  - **worldUnload compensation**: Cancelling `doClientStuff()` also skipped the `worldUnload` event registration (responsible for clearing the cached `SPIKE_DAMAGE` on world change). This mod now registers its own `LevelEvent.Unload` listener that clears MUG's private static `SPIKE_DAMAGE` via reflection, restoring original behavior.
  - **Shim approach rejected**: A same-package shim class (`mob_grinding_utils.events.BossBarHidingEvent`) was attempted first, but NeoForge 1.21.1's JPMS module layer rejects split packages between mod jars (`ResolutionException: Modules mob_grinding_utils and death_muffler_fix export package mob_grinding_utils.events`), so the Mixin approach is kept.

- **1.21.1 (NeoForge)**: Fixed dependency declaration for Mob Grinding Utils — changed from `optional` to `required` in `neoforge.mods.toml`. The mod imports MUG classes at load time and cannot function without it; the previous `optional` declaration would crash on missing MUG, contradicting the declared intent.

- **1.20.1 (Forge)**: Fixed dependency declaration — changed `type = "optional"` to `mandatory = true` in `mods.toml`. The original `type` field is not recognized by Forge 1.20.1's TOML parser (required field: `mandatory`), which caused the mod to be rejected as an invalid mod file entirely.
  - Also fixed the `forge` dependency entry to use the correct `mandatory = true` syntax.

- **All versions**: Added missing `pack.mcmeta` resource pack metadata.
  - Without `pack.mcmeta`, Forge 1.20.1 marks the mod's resource pack as broken and discards its resources (logo image fails to load; a warning is logged).
  - NeoForge 1.21.1 auto-generates default metadata, so this was non-critical there, but added for consistency.
  - Pack formats: 1.20.1 → `15`, 1.21.1 → `34`.

#### Changed

- Updated `README.md` to reflect the final 1.21.1 approach (Mixin + worldUnload compensation; shim rejected due to JPMS split-package restriction).

---

### 中文

#### 修复

- **1.21.1（NeoForge）**：完善基于 Mixin 的缺失类修复。MUG 1.1.10 发布的 jar 中 `BossBarHidingEvent` 只有 `.java` 源文件、没有编译 `.class`，导致客户端启动崩溃。Mixin 在 `new BossBarHidingEvent()` 指令前取消 `doClientStuff()`，血条隐藏由语言感知的监听器重新实现。
  - **worldUnload 补偿**：取消 `doClientStuff()` 同时跳过了 `worldUnload` 事件注册（负责在切换世界时清空缓存的 `SPIKE_DAMAGE`）。本模组现注册自己的 `LevelEvent.Unload` 监听器，经反射清空 MUG 的私有静态字段 `SPIKE_DAMAGE`，恢复原版行为。
  - **shim 方案弃用**：最初尝试同包同名 shim 补类（`mob_grinding_utils.events.BossBarHidingEvent`），但 NeoForge 1.21.1 的 JPMS 模块层禁止模组 jar 间拆分包（`ResolutionException: Modules mob_grinding_utils and death_muffler_fix export package mob_grinding_utils.events`），故保留 Mixin 方案。

- **1.21.1（NeoForge）**：修复 Mob Grinding Utils 依赖声明——在 `neoforge.mods.toml` 中将 `optional` 改为 `required`。本模组在加载时引用 MUG 类，未安装 MUG 时无法运行；原 `optional` 声明与实际行为矛盾（缺 MUG 时直接崩溃）。

- **1.20.1（Forge）**：修复依赖声明——在 `mods.toml` 中将 `type = "optional"` 改为 `mandatory = true`。Forge 1.20.1 的 TOML 解析器不识别 `type` 字段（必需字段为 `mandatory`），导致模组被标记为无效文件、完全无法加载。
  - 同时修正了 `forge` 依赖条目，使用正确的 `mandatory = true` 语法。

- **全版本**：补充缺失的 `pack.mcmeta` 资源包元数据文件。
  - 缺少 `pack.mcmeta` 时，Forge 1.20.1 会将模组的资源包标记为损坏并丢弃其资源（logo 图片无法加载，日志输出警告）。
  - NeoForge 1.21.1 会自动生成默认元数据，因此影响不大，但为一致性也一并添加。
  - Pack format：1.20.1 → `15`，1.21.1 → `34`。

#### 变更

- 更新 `README.md` 中 1.21.1 修复方式描述（Mixin + worldUnload 补偿；shim 方案因 JPMS 拆分包限制弃用）。