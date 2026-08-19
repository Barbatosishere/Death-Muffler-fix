# Death Muffler Fix — English

A client-side fix for the **Death Muffler** (Boss bar hiding) feature in [Mob Grinding Utils](https://www.curseforge.com/minecraft/mc-mods/mob-grinding-utils) by vadis365.

## The Problem

Mob Grinding Utils' Death Muffler is supposed to hide Wither and Ender Dragon boss bars when the player has the corresponding muffler upgrades installed. However, this feature is broken across versions:

| MC Version | MUG Version | Issue |
|---|---|---|
| 1.21.1 (NeoForge) | 1.1.10 | `BossBarHidingEvent` class was never compiled (only `.java` source shipped in the jar). Client initialization crashes with `NoClassDefFoundError` on startup. |
| 1.20.1 (Forge) | 1.1.0 | `BossBarHidingEvent` exists but matches boss names with hardcoded English strings (`"Wither"`, `"Dragon"`). Boss bar hiding fails in non-English locales (e.g. Chinese). |

## The Fix

| MC Version | Approach |
|---|---|
| **1.21.1** | Uses a **Mixin** to cancel `doClientStuff()` right before the `new BossBarHidingEvent()` instruction (the event registrations before it are unaffected), then re-registers the skipped tail logic (XP fluid render layers, color handlers, `worldUnload` damage-cache invalidation via reflection) and reimplements boss bar hiding with a language-aware listener. (A same-package shim class was attempted first, but NeoForge 1.21.1's JPMS module layer rejects split packages between mods.) |
| **1.20.1** | Registers an additional event listener that uses Minecraft's **localization system** (`I18n`) to match boss names. This supplements MUG's existing listener (which remains active for its extra logic like Wither Crumbs detection), so there is no conflict. |

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

## Notes

- For 1.21.1: The Mixin injection point is the `new BossBarHidingEvent()` instruction; if a future MUG release fixes `doClientStuff()` and the injection point disappears, this mod will fail fast at startup rather than crash obscurely.
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

## 修复方案

| MC 版本 | 方式 |
|---|---|
| **1.21.1** | 通过 **Mixin** 在 `new BossBarHidingEvent()` 指令前终止 `doClientStuff()`（其前段事件注册不受影响），随后补注册被跳过的尾部逻辑（XP 流体渲染层、颜色处理器、经反射实现的 `worldUnload` 伤害缓存失效），并以语言感知的监听器重新实现血条隐藏。（最初尝试同包同名 shim 补类，但 NeoForge 1.21.1 的 JPMS 模块层禁止模组间拆分包而无法采用。） |
| **1.20.1** | 注册一个额外的事件监听器，使用 Minecraft 的**语言系统**（`I18n`）进行 Boss 名称匹配。该监听器与 MUG 原版监听器叠加共存（原版仍保留其额外逻辑，如 Wither Crumbs 检测），互不冲突。 |

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

## 说明

- 1.21.1 版本：Mixin 注入点为 `new BossBarHidingEvent()` 指令；若 MUG 后续版本修复了 `doClientStuff()` 导致注入点消失，本模组会在启动时立即报错（fail-fast），而非晦涩崩溃。
- 本模组依赖实用设备（Mob Grinding Utils），未安装时将无法加载。