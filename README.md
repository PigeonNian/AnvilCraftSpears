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

## 原理

Backported Spears 的战斗逻辑全部由物品栈上的**数据组件**驱动，而不是写死在物品类里：

| 组件 | 作用 |
| --- | --- |
| `minecraft:attack_range` | 突刺的最小/最大距离 |
| `minecraft:piercing_weapon` | 突刺：穿透、击退、音效 |
| `minecraft:kinetic_weapon` | 冲锋：蓄力延迟、击落/击退/增伤条件、伤害倍率 |
| `minecraft:swing_animation` | 挥砍动画类型与时长 |
| `minecraft:minimum_attack_charge` | 攻击冷却门槛 |
| `minecraft:use_effects` | 持有时移动速度、能否冲刺 |

所以接入方式就是：**矛模式挂上这些组件，其它模式摘掉**。
组件同步发生在四个时机：模式切换（`SwitchHeavyHalberdModePacket` → `HeavyHalberdItem.setMode`）、物品从存档/网络加载、每 tick 兜底、以及右键时由 `use` 触发的使用状态。

## 使用方法

1. 安装 NeoForge 1.21.1，准备 AnvilCraft、AnvilLib 与 Backported Spears；
2. 把本模组与上述依赖一起放进 `mods` 文件夹；
3. 启动游戏，手持重戟切换到**矛模式**即可获得完整的矛机制。

## 构建

```powershell
# 编译打包（本机已有 Gradle 缓存时可加 --offline）
.\gradlew build

# 开发客户端 / 服务端（IDEA 里也有 .run/Run Client、.run/Run Server）
.\gradlew runClient
.\gradlew runServer
```

产物：`build/libs/anvilcraft_spears-neoforge-1.21.1-1.0.0.jar`

依赖解析需要能访问 `maven.neoforged.net` 与 `server.cjsah.net`；Backported Spears 使用 `libs/` 里自带的 1.21.1 NeoForge jar（`dependencies.gradle` 的 `spearsJar`）。

## 版本管理

版本号只有一个来源：`gradle.properties` 中的 `mod_version`。

| 场景 | 版本号 |
| --- | --- |
| 本地构建 | `1.0.0` |
| CI 快照（push 到 `master` / `dev/**` / `releases/**`） | `1.0.0+snapshot.<run_number>` |
| PR 构建 | `1.0.0+pr.<run_number>` |
| 打 tag 发版（`v1.0.0`） | `1.0.0` |

发版流程：

1. 修改 `gradle.properties` 的 `mod_version`；
2. 提交并推送；
3. 在 GitHub 上创建 tag（如 `v1.0.0`）并发布 Release。

tag 与 `mod_version` 不一致时构建会直接失败，避免“jar 里一套版本、仓库里另一套版本”。
`upload`、`modrinth_id`、`curseforge_id` 用于发布平台，未配置时 CI 只构建、不上传。

## 已验证

- `gradlew --offline build`：编译打包通过
- `gradlew runServer`：Mixin 成功注入 `HeavyHalberdItem`（日志可见 7 处 `@Inject`），专用服务器 `Done (15.6s)` 正常启动
- 运行时自检：重戟切到矛模式后 6 个组件全部就位、`getUseAnimation() = SPEAR`、`getUseDuration() = 72000`；切回剑模式后组件全部清除

## 已知边界

- **没有**把重戟加进 `#minecraft:spears`。那个标签会让矛模组在*所有*模式下都把重戟当矛（第三人称手臂姿势、GUI 渲染路径），与“只有矛模式才是矛”冲突，所以这里只走组件驱动。
- 重戟本体的“重锤下落猛击”逻辑在矛模式下依然存在（AnvilCraft 本体设计如此），需要的话可以再 mixin `hurtEnemy` 里按模式屏蔽。
- 若矛模组以后改了组件结构，优先检查 `HeavyHalberdSpearMode` 的组件构造参数。

## 主要维护者

[@PigeonNian](https://github.com/PigeonNian)

## 使用许可

* 除非另有说明，否则代码默认许可为 [LICENSE 文件(LGPL-3.0)](./LICENSE)
* 除非另有说明，否则非代码资产默认许可为 [ASSET_LICENSE 文件(ARR)](./ASSETS_LICENSE)
