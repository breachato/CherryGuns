# CherryGuns

The weapon system of a **Minecraft 1.20.6** server: real guns with magazines, reloads and headshots, body armour that stops bullets, grenades, tasers, a catalogue GUI, per-weapon whitelist and blacklist, and staff logs of every shot.

Every weapon is a definition loaded from disk: nothing about the guns is hardcoded, so a new one is created in game and stored in the plugin folder.

## What it does

- **Weapons, ammo and armour** — three separate loaders read weapons, magazines and armour pieces from disk and register them at startup; `/armi aggiungi` creates new ones in game.
- **Shooting** — hitscan rays with body-part detection: head, body and limbs take different damage. Ammo is consumed per shot, and a dry weapon has to be reloaded.
- **Ranged behaviour** — recoil and aim handled by the aim engine, click handling for semi and automatic fire, and a reload engine that respects the magazine size.
- **Grenades and tasers** — implemented as their own weapon types, with the same registry, permissions and logging as firearms.
- **Who can use what** — per-weapon whitelist and blacklist, on top of LuckPerms permissions; `cherryguns.armi.bypass` skips both.
- **Catalogue GUI** — `menu.yml` describes the layout (categories, then weapons, with back and page buttons), so the armeria can be rearranged without touching code.
- **Staff logs** — every shot, hit, death and taser use is tracked and sent to staff; `/cherryguns_tp` teleports to the place of a logged shot.
- **API for other plugins** — `ArmaSparoEvent`, `ArmaHeadshotEvent`, `ArmaColpoEntitaEvent`, `ArmaConsegnaEvent` and `ArmaInterazioneEvent`, plus a `CherryAPI` facade.
- **Protection hooks** — WorldGuard regions are respected, and the plugin stays quiet when WorldGuard is not installed.

## Commands

| Command | What it does |
| --- | --- |
| `/armi` (`/cg`, `/cherryguns`) | Main command |
| `/armi gui` | Opens the weapon catalogue |
| `/armi lista` | Lists the registered weapons |
| `/armi prendi <weapon> [player]` | Gives a weapon |
| `/armi cerca <text>` | Searches the registry |
| `/armi aggiungi` | Creates a new weapon from the item in your hand |
| `/armi rimuovi <weapon>` | Removes a weapon |
| `/armi whitelist <weapon> <player>` / `/armi blacklist ...` | Controls who may use a weapon |
| `/armi reload` | Reloads configuration, messages and the whole registry |
| `/cherryguns_tp <uuid>` | Teleports staff to a logged shot |

## Permissions

| Permission | Default | Grants |
| --- | --- | --- |
| `cherryguns.*` | op | Everything below |
| `cherryguns.usegun` | true | Using weapons |
| `cherryguns.armi.gui` | true | Opening the catalogue |
| `cherryguns.armi.prendi` | op | Taking weapons |
| `cherryguns.armi.aggiungi` / `rimuovi` / `cerca` | op | Managing the registry |
| `cherryguns.armi.whitelist` / `blacklist` | op | Access lists |
| `cherryguns.armi.bypass` | op | Ignores whitelist and blacklist |
| `cherryguns.logs` | op | Receives staff logs |
| `cherryguns.reload` | op | Reloading the plugin |

## Configuration

| File | Contents |
| --- | --- |
| `config.yml` | General settings, generated on first start |
| `lang.yml` | Every message, generated with the defaults |
| `menu.yml` | Catalogue layout: titles, rows, category and weapon slots |
| `armi/`, `munizioni/`, `armature/` | The weapon, ammo and armour definitions, written from in game |

## Requirements

- Paper or Spigot **1.20.6**, Java **17**.
- Optional: **LuckPerms** (permissions), **WorldGuard** (regions), **ProtocolLib**.
- Bundled at build time by the shadow jar: **XSeries**, **item-nbt-api**, **worldguardwrapper**, **inventorygui**.

## Build

```bash
./gradlew shadowJar
```

The jar lands in `build/libs/CherryGuns.jar`. Drop it in `plugins/` and restart.

## Files

| Path | What it is |
| --- | --- |
| `CherryGuns.java` | Plugin entry point: configuration, hooks, loaders, listeners |
| `api/` | `CherryAPI` and the events other plugins can listen to |
| `armi/base/` | Registry and the base objects every weapon shares |
| `armi/tipi/` | Weapon, armour, grenade, taser and ammo types |
| `armi/motore/` | Firing, aiming, reloading, clicks, body-part detection |
| `armi/persistenza/` | Loaders for weapons, ammo and armour |
| `armi/accesso/` | `/armi` command, weapon creator, access lists, staff teleport |
| `interfaccia/` | Catalogue menus and their elements |
| `log/` | Shot tracking and the staff log |
| `configurazione/` | Config, messages, item builder |
| `hook/` | LuckPerms, WorldGuard and economy integration |
| `src/main/resources/` | `plugin.yml`, `menu.yml` |

## License

The source is visible because the repository is public, but it is **not** open source: proprietary, all rights reserved. See [`LICENSE.md`](./LICENSE.md).
