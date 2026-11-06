# AnvilCraft: Spears

把 AnvilCraft **重戟（Heavy Halberd）的矛模式**接进 [Backported Spears](https://github.com/Unknowneth/Backported-Spears)。
不新增任何物品——只是让原本“有空壳、没机制”的矛模式真正变成一把矛。

- Minecraft 1.21.1 / NeoForge 21.1.238
- AnvilCraft 1.6.0+snapshot.2713、AnvilLib 2.0.0+snapshot.565、Ageratum 0.0.1+build.121
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

## 代码结构

```
src/main/java/dev/anvilcraft/addon/spears/
├── AnvilCraftSpears.java            入口（只有日志，没有新内容注册）
├── HeavyHalberdSpearMode.java       组件桥接：isSpearMode / sync / 组件数值
└── mixin/
    └── HeavyHalberdItemMixin.java   use、getUseAnimation、getUseDuration、组件同步、lunge 门槛

src/main/resources/
├── anvilcraft_spears.mixins.json
└── data/minecraft/tags/item/enchantable/lunge.json   让重戟能吃 lunge 附魔
```

## 调参

改 `HeavyHalberdSpearMode.SpearComponents` 里的常量即可：

- `SWING_ANIMATION`：`20` tick 是挥砍时长，重戟比普通矛慢
- `KINETIC_WEAPON`：第 3 个参数是蓄力延迟 tick，后面三个 `Condition` 分别是击落骑乘 / 击退 / 增伤的速度门槛与最长蓄力时间
- `1.35F` 是冲锋伤害倍率，想更“重”可以调到 1.5

## 构建与运行

```powershell
# 编译打包（本机已有 Gradle 缓存时可加 --offline）
.\gradlew build

# 开发客户端 / 服务端（IDEA 里也有 .run/Run Client、.run/Run Server）
.\gradlew runClient
.\gradlew runServer
```

产物：`build/libs/anvilcraft_spears-neoforge-1.21.1-0.1.0.jar`

依赖解析需要能访问 `maven.neoforged.net` 与 `server.cjsah.net`；Backported Spears 用 `libs/` 里自带的 1.21.1 NeoForge jar（`dependencies.gradle` 的 `spearsJar`）。

## 已验证

- `gradlew --offline build`：编译打包通过
- `gradlew runServer`：Mixin 成功注入 `HeavyHalberdItem`（日志可见 7 处 `@Inject`），专用服务器 `Done (15.6s)` 正常启动
- 运行时自检：重戟切到矛模式后 6 个组件全部就位、`getUseAnimation() = SPEAR`、`getUseDuration() = 72000`；切回剑模式后组件全部清除

## 已知边界

- **没有**把重戟加进 `#minecraft:spears`。那个标签会让矛模组在*所有*模式下都把重戟当矛（第三人称手臂姿势、GUI 渲染路径），与“只有矛模式才是矛”冲突，所以这里只走组件驱动。
- 重戟本体的“重锤下落猛击”逻辑在矛模式下依然存在（AnvilCraft 本体设计如此），需要的话可以再 mixin `hurtEnemy` 里按模式屏蔽。
- 若矛模组以后改了组件结构，优先检查 `HeavyHalberdSpearMode` 的组件构造参数。

## 许可

MIT。AnvilCraft（LGPL-3.0）与 Backported Spears 仅作为依赖引用，不包含在本仓库中。
