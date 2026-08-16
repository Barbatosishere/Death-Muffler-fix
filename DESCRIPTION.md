# Death Muffler Fix — English

A client-side fix for the **Death Muffler** (Boss bar hiding) feature in [Mob Grinding Utils](https://www.curseforge.com/minecraft/mc-mods/mob-grinding-utils) by vadis365.

## The Problem

Mob Grinding Utils' Death Muffler is supposed to hide Wither and Ender Dragon boss bars when the player has the corresponding muffler upgrades installed. However, this feature is broken across all supported versions:

| MC Version | MUG Version | Issue |
|---|---|---|
| 1.21.1 (NeoForge) | 1.1.10 | `BossBarHidingEvent` class was never compiled (only `.java` source shipped in the jar). Client initialization crashes with `NoClassDefFoundError` on startup. |
| 1.20.1 (Forge) | 1.1.0 | `BossBarHidingEvent` exists but matches boss names with hardcoded English strings (`"Wither"`, `"Dragon"`). Boss bar hiding fails in non-English locales (e.g. Chinese). |
| 1.12.x (Forge) | 0.3.13 | Same hardcoded English name matching issue. Also uses exact `equals()` instead of `contains()`, so any custom boss name or translation breaks the match. |

## The Fix

| MC Version | Approach |
|---|---|
| **1.21.1** | Ships a **shim class** (`mob_grinding_utils.events.BossBarHidingEvent`) compiled into this mod's jar, filling the missing bytecode in MUG 1.1.10. MUG's `doClientStuff()` then runs to completion — boss bar hiding, world unload cleanup, XP fluid render layers, and color handlers all work as originally intended. No Mixin or logic patching needed. |
| **1.20.1 / 1.12.x** | Registers an additional event listener that uses Minecraft's **localization system** (`I18n`) to match boss names. This supplements MUG's existing listener (which remains active for its extra logic like Wither Crumbs detection), so there is no conflict. |

## Features

- Fixes Wither boss bar hiding for the Death Muffler
- Fixes Ender Dragon boss bar hiding for the Death Muffler
- Fully compatible with all languages (uses Minecraft's built-in translation keys)
- Client-side only — works on any server
- No configuration required

## Requirements

| Version | Loader | Mob Grinding Utils | Java |
|---|---|---|---|
| 1.21.1 | NeoForge 21.1.230+ | 1.1.10+ | 21 |
| 1.20.1 | Forge 47.1.106+ | 1.1.0+ | 17 |
| 1.12.x (1.12/1.12.1/1.12.2) | Forge 14.21+ | 0.3.13+ | 8 |

## Notes

- For 1.21.1: If MUG releases a future version that includes the compiled `BossBarHidingEvent` class, both jars will contain the same class. Since the implementations are identical, this causes no conflict.
- This mod requires Mob Grinding Utils to be installed. It will not load without it.

---

# Death Muffler Fix — 中文

修复 [实用设备（Mob Grinding Utils）](https://www.curseforge.com/minecraft/mc-mods/mob-grinding-utils)（作者 vadis365）中**消声器（Death Muffler）** Boss 血条隐藏功能的一个客户端修复模组。

## 问题背景

实用设备的消声器本应在玩家安装对应消音升级后隐藏凋灵和末影龙的 Boss 血条，但该功能在各版本中均存在问题：

| MC 版本 | MUG 版本 | 问题 |
|---|---|---|
| 1.21.1（NeoForge） | 1.1.10 | `BossBarHidingEvent` 类从未被编译（jar 中仅附带了 `.java` 源文件）。客户端启动时执行到 `new BossBarHidingEvent()` 即抛出 `NoClassDefFoundError`，直接崩溃。 |
| 1.20.1（Forge） | 1.1.0 | `BossBarHidingEvent` 类存在，但使用硬编码的英文字符串（`"Wither"`、`"Dragon"`）匹配 Boss 名称，非英文环境下（如中文）血条隐藏完全失效。 |
| 1.12.x（Forge） | 0.3.13 | 同样的硬编码英文名匹配问题，且使用 `equals()` 精确匹配而非 `contains()` 包含匹配，任何自定义 Boss 名称或本地化翻译都会导致匹配失败。 |

## 修复方案

| MC 版本 | 方式 |
|---|---|
| **1.21.1** | 在本模组 jar 中编译了一份**同包同名 shim 类**（`mob_grinding_utils.events.BossBarHidingEvent`），补齐 MUG 1.1.10 中缺失的字节码。MUG 的 `doClientStuff()` 从此可以完整执行——Boss 血条隐藏、世界卸载清理、XP 流体渲染层、颜色处理器全部按原逻辑生效，无需 Mixin 或逻辑补偿。 |
| **1.20.1 / 1.12.x** | 注册一个额外的事件监听器，使用 Minecraft 的**语言系统**（`I18n`）进行 Boss 名称匹配。该监听器与 MUG 原版监听器叠加共存（原版仍保留其额外逻辑，如 Wither Crumbs 检测），互不冲突。 |

## 功能特性

- 修复消声器的凋灵（Wither）Boss 血条隐藏功能
- 修复消声器的末影龙（Ender Dragon）Boss 血条隐藏功能
- 使用 Minecraft 内置翻译键匹配 Boss 名称，兼容所有语言环境
- 纯客户端模组——在任何服务器上均可使用
- 无需配置，安装即生效

## 依赖要求

| 版本 | 加载器 | 实用设备（MUG） | Java |
|---|---|---|---|
| 1.21.1 | NeoForge 21.1.230+ | 1.1.10+ | 21 |
| 1.20.1 | Forge 47.1.106+ | 1.1.0+ | 17 |
| 1.12.x (1.12/1.12.1/1.12.2) | Forge 14.21+ | 0.3.13+ | 8 |

## 说明

- 1.21.1 版本：若 MUG 后续更新补全了 `BossBarHidingEvent` 的编译类，两个 jar 中将出现同名类。由于实现完全一致，不会产生冲突。
- 本模组依赖实用设备（Mob Grinding Utils），未安装时将无法加载。
