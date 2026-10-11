# ItemTag for Minecraft 26.3 and Folia

ItemTag extends ItemEdit with custom item flags, equipment effects and item actions.
This port targets Paper/Folia 26.3 and requires Java 25 and ItemEdit 3.7.10 or later.
The original upstream license and author are preserved.

## Installation

1. Keep a backup of your existing ItemTag folder and old plugin JAR.
2. Replace the old ItemTag JAR with `ItemTag-3.7.7-folia.1.jar`.
3. Keep ItemEdit installed. The port was compiled with ItemEdit 3.7.10.
4. Restart the server. Keep the existing ItemTag configuration and item data.

The default `data.preference: SPIGOT` stores tags in Bukkit's PersistentDataContainer
and needs no NBTAPI plugin. Existing tag names and formats are unchanged. If selecting
`NBTAPI`, install a version that supports your server; the compile dependency is 2.16.1.
An incompatible or absent NBTAPI falls back to the standard backend with a warning.
Switching backends does not convert existing tags from one backend to the other.

## Folia changes

- Equipment polling dispatches checks to each player's entity scheduler. The existing
  frequency and maximum players per tick settings remain available.
- Delayed actions, post-respawn effects, milk/totem effect restoration and inventory
  corrections follow the player's scheduler across regions and worlds.
- Shared equipment, drop tracking, tag key and activity caches use concurrent maps.
- Reload invalidates old equipment callbacks and rebuilds each player's cache on their
  own region. Disable cancels polling and unregisters the persistent PAPI expansion.
- Console item actions expand player placeholders on the player's region and dispatch
  on the global region. They can execute a tick later on Folia. Commands from other
  plugins must also support Folia.
- Held-item PlaceholderAPI values use immutable cached snapshots outside the player's
  region, updated by equipment events and polling. Owner-region requests read live data.
- Temporary operator action recovery records are synchronized and saved after cleanup.
- Existing unfinished activity/trigger commands remain disabled as in the supplied source.

## Building

Use Maven and Java 25. If ItemEdit is not already in your Maven repository, install the
provided dependency from your ItemEdit JAR first:

```shell
mvn install:install-file -Dfile=/path/to/ItemEdit-3.7.10.jar -DgroupId=emanondev.itemedit -DartifactId=ItemEdit -Dversion=3.7.10 -Dpackaging=jar -DgeneratePom=true
mvn -Dmaven.test.skip=true package
```

Output: `target/ItemTag-3.7.7-folia.1.jar`. Optional API dependencies are not bundled.

Validation performed: compilation and packaging against Paper API 26.3.build.174-beta.
No automated tests or live server checks were run.

Scheduler implementation follows the [Paper/Folia scheduler documentation](https://docs.papermc.io/paper/dev/folia-support/).
