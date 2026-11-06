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
- 
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

## Maintainer

[@PigeonNian](https://github.com/PigeonNian)

## License

* Code unless otherwise stated defaults to the [LICENSE file (LGPL-3.0)](./LICENSE)
* Non-code assets unless otherwise stated default to the [ASSET_LICENSE file (ARR)](./ASSETS_LICENSE)
