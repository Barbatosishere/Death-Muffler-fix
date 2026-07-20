# Death Muffler Fix

一个用于修复 **Mob Grinding Utils** 消声器（Death Muffler）Boss 血条隐藏失效问题的 Minecraft NeoForge 模组。

## 问题背景

Mob Grinding Utils 1.1.10 版本中，消声器（Death Muffler）的 Boss 血条隐藏功能因缺少 `BossBarHidingEvent` 类而失效。本模组通过 Mixin 和事件监听的方式重新实现了该功能。

## 功能特性

- 修复消声器的 **凋灵（Wither）** Boss 血条隐藏功能
- 修复消声器的 **末影龙（Ender Dragon）** Boss 血条隐藏功能
- 使用 Minecraft 内置语言系统进行 Boss 名称匹配，兼容多语言环境

## 依赖要求

| 依赖 | 版本 | 类型 |
|------|------|------|
| NeoForge | 21+ | 必需 |
| Minecraft | 1.21.1 | 必需 |
| Mob Grinding Utils | 1.1.10+ | 可选 |
| Java | 21+ | 必需 |

## 构建

```bash
# 克隆仓库
git clone https://github.com/Barbatosishere/Death-Muffler-fix.git
cd Death-Muffler-fix

# 构建模组
./gradlew build
```

构建产物位于 `build/libs/` 目录。

## 安装

将构建生成的 `.jar` 文件放入 Minecraft 的 `mods` 文件夹即可。

## 许可证

本项目采用 [GPL-3.0 License](LICENSE) 开源。

## 作者

- **Barbatosishere**
