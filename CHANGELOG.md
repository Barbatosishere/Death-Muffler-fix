# Changelog / 更新日志

All notable changes to Death Muffler Fix will be documented in this file.

---

## [1.0.0] — 2026-08-16

### English

#### Fixed

- **1.21.1 (NeoForge)**: Replaced Mixin-based workaround with a shim class approach. The missing `BossBarHidingEvent` (only `.java` source was shipped in MUG 1.1.10, no compiled `.class`) is now provided as a same-package shim compiled into this mod's jar. MUG's `doClientStuff()` runs to completion — boss bar hiding, `worldUnload` listener, XP fluid render layers, and color handler registration all work as originally intended.
  - **Previous approach bug**: The old Mixin cancelled `doClientStuff()` before `new BossBarHidingEvent()`, which also skipped the `worldUnload` event registration (responsible for clearing cached `SPIKE_DAMAGE` on world change). This cache invalidation logic was not compensated, causing stale damage data after dimension switches.
  - Removed `DoClientStuffMixin.java`, `ClientHandler.java`, and `death_muffler_fix.mixins.json` — no longer needed.
  - `Death_muffler_fix.java` simplified to a minimal `@Mod` entry point.

- **1.21.1 (NeoForge)**: Fixed dependency declaration for Mob Grinding Utils — changed from `optional` to `required` in `neoforge.mods.toml`. The mod imports MUG classes at load time and cannot function without it; the previous `optional` declaration would crash on missing MUG, contradicting the declared intent.
  - Removed `[[mixins]]` block from `neoforge.mods.toml` (no more mixins).

- **1.20.1 (Forge)**: Fixed dependency declaration — changed `type = "optional"` to `mandatory = true` in `mods.toml`. The original `type` field is not recognized by Forge 1.20.1's TOML parser (required field: `mandatory`), which caused the mod to be rejected as an invalid mod file entirely.
  - Also fixed the `forge` dependency entry to use the correct `mandatory = true` syntax.

- **1.12.x (Forge)**: Fixed dependency declaration — changed `mandatory = false` to `mandatory = true` for Mob Grinding Utils in `mcmod.info`. `ClientHandler` directly imports MUG capability classes and will throw `NoClassDefFoundError` on any boss bar render event if MUG is absent.

- **1.12.x (Forge)**: Declared compatibility with the whole Minecraft 1.12.x family (1.12 / 1.12.1 / 1.12.2): added `acceptedMinecraftVersions = "[1.12,1.13)"` to the `@Mod` annotation, relaxed the Forge dependency range from `[14.23.5.2847,)` to `[14.21,)`, and set `mcversion` to `1.12.x`. Upstream MUG 0.3.13 is itself built against 1.12.2 and works across the family the same way; the build toolchain remains 1.12.2 (RFG limitation, bytecode-compatible).

- **All versions**: Added missing `pack.mcmeta` resource pack metadata.
  - Without `pack.mcmeta`, Forge 1.20.1 marks the mod's resource pack as broken and discards its resources (logo image fails to load; a warning is logged).
  - NeoForge 1.21.1 auto-generates default metadata, so this was non-critical there, but added for consistency.
  - Pack formats: 1.12.x → `3`, 1.20.1 → `15`, 1.21.1 → `34`.

#### Changed

- Updated `README.md` to reflect the new 1.21.1 shim class approach.

---

### 中文

#### 修复

- **1.21.1（NeoForge）**：将基于 Mixin 的变通方案替换为 shim 补类方案。MUG 1.1.10 发布的 jar 中 `BossBarHidingEvent` 只有 `.java` 源文件、没有编译 `.class`，导致客户端启动崩溃。现在将该缺失类以同包同名 shim 的形式编译进本模组 jar，MUG 的 `doClientStuff()` 完整执行——Boss 血条隐藏、`worldUnload` 监听（切换世界时清空 `SPIKE_DAMAGE` 缓存）、XP 流体渲染层、颜色处理器注册全部按原逻辑生效。
  - **原方案缺陷**：旧版 Mixin 在 `new BossBarHidingEvent()` 前取消了 `doClientStuff()` 方法，但同时跳过了 `worldUnload` 事件注册（负责在切换世界时清空缓存的 `SPIKE_DAMAGE`），该缓存失效逻辑未被补偿，导致跨维度后伤害数据陈旧。
  - 已移除 `DoClientStuffMixin.java`、`ClientHandler.java` 和 `death_muffler_fix.mixins.json`——不再需要。
  - `Death_muffler_fix.java` 精简为最小 `@Mod` 入口。

- **1.21.1（NeoForge）**：修复 Mob Grinding Utils 依赖声明——在 `neoforge.mods.toml` 中将 `optional` 改为 `required`。本模组在加载时引用 MUG 类，未安装 MUG 时无法运行；原 `optional` 声明与实际行为矛盾（缺 MUG 时直接崩溃）。
  - 已移除 `[[mixins]]` 块（不再使用 Mixin）。

- **1.20.1（Forge）**：修复依赖声明——在 `mods.toml` 中将 `type = "optional"` 改为 `mandatory = true`。Forge 1.20.1 的 TOML 解析器不识别 `type` 字段（必需字段为 `mandatory`），导致模组被标记为无效文件、完全无法加载。
  - 同时修正了 `forge` 依赖条目，使用正确的 `mandatory = true` 语法。

- **1.12.x（Forge）**：修复依赖声明——在 `mcmod.info` 中将 Mob Grinding Utils 的 `mandatory` 从 `false` 改为 `true`。`ClientHandler` 直接引用 MUG 能力类，未安装 MUG 时任何 Boss 血条渲染事件都会抛出 `NoClassDefFoundError`。

- **1.12.x（Forge）**：声明兼容整个 Minecraft 1.12.x 系列（1.12 / 1.12.1 / 1.12.2）：`@Mod` 注解添加 `acceptedMinecraftVersions = "[1.12,1.13)"`，Forge 依赖范围从 `[14.23.5.2847,)` 放宽为 `[14.21,)`，`mcversion` 改为 `1.12.x`。上游 MUG 0.3.13 本身也是基于 1.12.2 构建并跨系列工作的；构建工具链保持 1.12.2（RFG 限制，字节码兼容）。

- **全版本**：补充缺失的 `pack.mcmeta` 资源包元数据文件。
  - 缺少 `pack.mcmeta` 时，Forge 1.20.1 会将模组的资源包标记为损坏并丢弃其资源（logo 图片无法加载，日志输出警告）。
  - NeoForge 1.21.1 会自动生成默认元数据，因此影响不大，但为一致性也一并添加。
  - Pack format：1.12.x → `3`，1.20.1 → `15`，1.21.1 → `34`。

#### 变更

- 更新 `README.md` 中 1.21.1 修复方式描述，反映新的 shim 补类方案。
