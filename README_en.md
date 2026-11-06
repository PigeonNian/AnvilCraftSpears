<div align="center" style="align-items: center">

## Minecraft mod by [鸽の念@bilibili](https://space.bilibili.com/23416735)

# AnvilCraft: Spears | [铁砧工艺：矛](./README.md)

![Java21](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java21_vector.svg)
![NeoForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg)

[![AnvilLib](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/mc/anvillib_vector.svg)][AnvilLib]
[![AnvilCraft](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/mc/anvilcraft_vector.svg)][AnvilCraft]

![Unsupported Fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/fabric_vector.svg)
![Unsupported Forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/forge_vector.svg)

### License

[![License](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/license/lgpl-v3_vector.svg)][License]

### Asset License

[![Asset License](https://cdn.jsdelivr.net/npm/@gugle/devins-badges-plus/assets/cozy/license/arr_vector.svg)][Asset License]

[AnvilLib]: https://lib.anvilcraft.dev

[AnvilCraft]: https://www.anvilcraft.dev

[License]: https://spdx.org/licenses/LGPL-3.0-or-later.html

[Asset License]: https://gist.github.com/Gu-ZT/38961ed5c97500cf61b04ab048fa38ad
</div>

## Introduction

Hooks the **spear mode of AnvilCraft's Heavy Halberd** into [Backported Spears](https://github.com/Unknowneth/Backported-Spears).
No new items are added — it simply turns the previously hollow spear mode into a real spear.

- Minecraft 1.21.1 / NeoForge 21.1.238
- AnvilCraft 1.6.0+, AnvilLib 2.0.0+
- Backported Spears 1.8.0

## What spear mode gives you

The heavy halberd stores its mode in `CUSTOM_MODEL_DATA`: `0 = trident`, `1 = spear`, `2 = sword`, `3 = hammer`.

| Mechanic | Behaviour |
| --- | --- |
| Stab (left click) | 2.0 ~ 4.5 block reach, hits multiple targets, dedicated sound |
| Charge (hold right click) | Starts after a 0.5 s wind-up: speed ≥ 6.5 dismounts riders, ≥ 4.8 knocks back, relative speed ≥ 4.2 deals 1.35× charge damage |
| Swing animation | `stab` type, 20 ticks; first-person raise and third-person thrust poses |
| Enchanting | Accepts the spear mod's `lunge` enchantment, only active in spear mode |
| Handling | No movement slowdown and sprinting allowed while held (`use_effects`) |

The other three modes (trident / sword / hammer) stay **completely unchanged**: the components are removed when you leave spear mode.

## How it works

All combat logic in Backported Spears is driven by **data components** on the item stack rather than hard-coded in the item class:

| Component | Purpose |
| --- | --- |
| `minecraft:attack_range` | Min/max stab reach |
| `minecraft:piercing_weapon` | Stab: piercing, knockback, sound |
| `minecraft:kinetic_weapon` | Charge: wind-up delay, dismount/knockback/damage conditions, multiplier |
| `minecraft:swing_animation` | Swing animation type and duration |
| `minecraft:minimum_attack_charge` | Attack cooldown threshold |
| `minecraft:use_effects` | Movement speed and sprinting while held |

So the integration is simply: **attach these components in spear mode, remove them otherwise**.
Syncing happens on mode switch (`SwitchHeavyHalberdModePacket` → `HeavyHalberdItem.setMode`), on item load, every tick as a fallback, and when `use` starts the charging state.

## Usage

1. Install NeoForge 1.21.1 together with AnvilCraft, AnvilLib and Backported Spears;
2. Put this mod and the dependencies above into the `mods` folder;
3. Launch the game and switch the heavy halberd to **spear mode**.

## Building

```powershell
# Compile and package (add --offline if the Gradle cache is warm)
.\gradlew build

# Development client / server (also available in IDEA under .run/)
.\gradlew runClient
.\gradlew runServer
```

Output: `build/libs/anvilcraft_spears-neoforge-1.21.1-1.0.0.jar`

Dependency resolution requires access to `maven.neoforged.net` and `server.cjsah.net`; Backported Spears uses the bundled 1.21.1 NeoForge jar in `libs/` (see `spearsJar` in `dependencies.gradle`).

## Versioning

The only source of truth for the version is `mod_version` in `gradle.properties`.

| Situation | Version |
| --- | --- |
| Local build | `1.0.0` |
| CI snapshot (push to `master` / `dev/**` / `releases/**`) | `1.0.0+snapshot.<run_number>` |
| Pull request build | `1.0.0+pr.<run_number>` |
| Tagged release (`v1.0.0`) | `1.0.0` |

Release process:

1. Bump `mod_version` in `gradle.properties`;
2. Commit and push;
3. Create a tag (e.g. `v1.0.0`) and publish a GitHub Release.

The build fails when the tag and `mod_version` disagree, so the jar and the repository can never report different versions.
`upload`, `modrinth_id` and `curseforge_id` control publishing; leave them unset to build without uploading.

## Verified

- `gradlew --offline build`: compiles and packages successfully
- `gradlew runServer`: the mixin injects into `HeavyHalberdItem` (7 `@Inject` points in the log) and the dedicated server reaches `Done (15.6s)`
- Runtime self-check: all 6 components are present in spear mode, `getUseAnimation() = SPEAR`, `getUseDuration() = 72000`; they are all removed after switching back to sword mode

## Known limitations

- The heavy halberd is **not** added to `#minecraft:spears`. That tag would make the spear mod treat it as a spear in *all* modes (third-person arm pose, GUI render path), which conflicts with "only spear mode is a spear"; the component-driven approach is used instead.
- AnvilCraft's own "hammer slam" logic still runs in spear mode (by design in the base mod). It can be gated by mode with an additional `hurtEnemy` mixin if desired.
- If Backported Spears changes its component layout in the future, check the component constructors in `HeavyHalberdSpearMode` first.

## Maintainer

[@PigeonNian](https://github.com/PigeonNian)

## License

* Code unless otherwise stated defaults to the [LICENSE file (LGPL-3.0)](./LICENSE)
* Non-code assets unless otherwise stated default to the [ASSET_LICENSE file (ARR)](./ASSETS_LICENSE)
