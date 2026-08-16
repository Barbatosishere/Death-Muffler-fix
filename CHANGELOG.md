# Changelog / 更新日志

All notable changes to Death Muffler Fix will be documented in this file.

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

- **1.12.x (Forge)**: Fixed dependency declaration — changed `mandatory = false` to `mandatory = true` for Mob Grinding Utils in `mcmod.info`. `ClientHandler` directly imports MUG capability classes and will throw `NoClassDefFoundError` on any boss bar render event if MUG is absent.

- **1.12.x (Forge)**: Declared compatibility with the whole Minecraft 1.12.x family (1.12 / 1.12.1 / 1.12.2): added `acceptedMinecraftVersions = "[1.12,1.13)"` to the `@Mod` annotation, relaxed the Forge dependency range from `[14.23.5.2847,)` to `[14.21,)`, and set `mcversion` to `1.12.x`. Upstream MUG 0.3.13 is itself built against 1.12.2 and works across the family the same way; the build toolchain remains 1.12.2 (RFG limitation, bytecode-compatible).

- **All versions**: Added missing `pack.mcmeta` resource pack metadata.
  - Without `pack.mcmeta`, Forge 1.20.1 marks the mod's resource pack as broken and discards its resources (logo image fails to load; a warning is logged).
  - NeoForge 1.21.1 auto-generates default metadata, so this was non-critical there, but added for consistency.
  - Pack formats: 1.12.x → `3`, 1.20.1 → `15`, 1.21.1 → `34`.

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

- **1.12.x（Forge）**：修复依赖声明——在 `mcmod.info` 中将 Mob Grinding Utils 的 `mandatory` 从 `false` 改为 `true`。`ClientHandler` 直接引用 MUG 能力类，未安装 MUG 时任何 Boss 血条渲染事件都会抛出 `NoClassDefFoundError`。

- **1.12.x（Forge）**：声明兼容整个 Minecraft 1.12.x 系列（1.12 / 1.12.1 / 1.12.2）：`@Mod` 注解添加 `acceptedMinecraftVersions = "[1.12,1.13)"`，Forge 依赖范围从 `[14.23.5.2847,)` 放宽为 `[14.21,)`，`mcversion` 改为 `1.12.x`。上游 MUG 0.3.13 本身也是基于 1.12.2 构建并跨系列工作的；构建工具链保持 1.12.2（RFG 限制，字节码兼容）。

- **全版本**：补充缺失的 `pack.mcmeta` 资源包元数据文件。
  - 缺少 `pack.mcmeta` 时，Forge 1.20.1 会将模组的资源包标记为损坏并丢弃其资源（logo 图片无法加载，日志输出警告）。
  - NeoForge 1.21.1 会自动生成默认元数据，因此影响不大，但为一致性也一并添加。
  - Pack format：1.12.x → `3`，1.20.1 → `15`，1.21.1 → `34`。

#### 变更

- 更新 `README.md` 中 1.21.1 修复方式描述（Mixin + worldUnload 补偿；shim 方案因 JPMS 拆分包限制弃用）。
