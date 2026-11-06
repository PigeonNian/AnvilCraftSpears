<div align="center" style="align-items: center">

## Minecraft模组 by [鸽の念@bilibili](https://space.bilibili.com/23416735)

# 铁砧工艺：矛 | [AnvilCraft: Spears](./README_en.md)

![Java21](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java21_vector.svg)
![NeoForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg)

[![AnvilLib](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/mc/anvillib_vector.svg)][AnvilLib]
[![AnvilCraft](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/mc/anvilcraft_vector.svg)][AnvilCraft]

![Unsupported Fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/fabric_vector.svg)
![Unsupported Forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/forge_vector.svg)

### 许可协议

[![License](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/license/lgpl-v3_vector.svg)][License]

### 资源许可协议

[![Asset License](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/license/arr_vector.svg)][Asset License]

[AnvilLib]: https://lib.anvilcraft.dev

[AnvilCraft]: https://www.anvilcraft.dev

[License]: https://spdx.org/licenses/LGPL-3.0-or-later.html

[Asset License]: https://gist.github.com/Gu-ZT/38961ed5c97500cf61b04ab048fa38ad
</div>

## 简介

把 AnvilCraft **重戟（Heavy Halberd）的矛模式**接进 [Backported Spears](https://github.com/Unknowneth/Backported-Spears)。
不新增任何物品——只是让原本“有空壳、没机制”的矛模式真正变成一把矛。

- Minecraft 1.21.1 / NeoForge 21.1.238
- AnvilCraft 1.6.0+、AnvilLib 2.0.0+
- Backported Spears 1.8.0

## 矛模式下会得到什么

重戟用 `CUSTOM_MODEL_DATA` 存模式：`0 = 三叉戟`、`1 = 矛`、`2 = 剑`、`3 = 重锤`。

| 机制 | 表现 |
| --- | --- |
| 突刺（左键） | 攻击距离 2.0 ~ 4.5 格，可穿透多个目标，带专属音效 |
| 冲锋（右键蓄力） | 蓄力 0.5 秒后开始判定：速度 ≥ 6.5 可击落骑乘，≥ 4.8 可击退，相对速度 ≥ 4.2 时造成 1.35 倍冲锋伤害 |
| 挥砍动画 | 换成 `stab` 类型、20 tick；第一人称有抬矛动作，第三人称有突刺姿势 |
| 附魔 | 可附矛模组的 `lunge`（突刺）附魔，且只在矛模式生效 |
| 使用手感 | 持矛时移动不减速、可冲刺（`use_effects` 组件） |

其它三种模式（三叉戟 / 剑 / 重锤）行为**完全不变**：离开矛模式时组件会被摘除。
## 使用方法

1. 安装 NeoForge 1.21.1，准备 AnvilCraft、AnvilLib 与 Backported Spears；
2. 把本模组与上述依赖一起放进 `mods` 文件夹；
3. 启动游戏，手持重戟切换到**矛模式**即可获得完整的矛机制。
## 主要维护者

[@PigeonNian](https://github.com/PigeonNian)

## 使用许可

* 除非另有说明，否则代码默认许可为 [LICENSE 文件(LGPL-3.0)](./LICENSE)
* 除非另有说明，否则非代码资产默认许可为 [ASSET_LICENSE 文件(ARR)](./ASSETS_LICENSE)
