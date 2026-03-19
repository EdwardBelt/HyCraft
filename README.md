# HyCraft

**HyCraft** is a Hytale server plugin that acts as a protocol bridge, allowing **Minecraft Java Edition (1.21.11)** clients to connect directly to Hytale servers.

It functions by spinning up a parallel TCP server within the Hytale server process. It intercepts incoming Minecraft packets, translates them on-the-fly into Hytale's internal protocol, and forwards them to the game engine. This allows Minecraft players to join Hytale worlds without any client-side modifications.

> **⚠️ Early Development Status**
>
> This project is currently in an **Early Development** stage. Features are incomplete, bugs are expected, and the API is subject to change.
>
> **Test Server:** `mytale.online` (on default MC port 25565, and default Hytale port 5520)

## Features

*   **Protocol Translation:** Real-time translation between Minecraft 1.21.11 (Protocol 774) and Hytale.
*   **Authentication:** Full Mojang authentication support (Online Mode) via `sessionserver.mojang.com`.
*   **World Rendering:**
    *   Dynamic chunk conversion (Hytale chunks $\to$ Minecraft chunks).
    *   Block palette mapping.
    *   Biome translation.
*   **Entity Synchronization:**
    *   Entity spawning, movement, and rotation.
    *   Metadata synchronization (names, health, equipment).
    *   Animation translation (swinging, damage, death).
*   **Interactions:**
    *   Block breaking and placing logic.
    *   PVP and PvE combat system (calculating damage, knockback, and cooldowns).
    *   Complex interaction handling (chaining, charging interactions).
*   **Inventory:**
    *   Hotbar and inventory synchronization.
    *   Item mapping (Hytale items $\to$ Minecraft equivalents).

...and more to come!

## Installation

### Prerequisites
*   A Hytale Server instance.
*   **Java 25**.

### Setup
1.  Download or build the latest `HyCraft-1.0-SNAPSHOT.jar` release.
2.  Navigate to your Hytale server directory.
3.  Place the jar file into the `mods/` folder.
4.  Start the Hytale server.
5.  HyCraft will start a Minecraft listener on port `25565` (configurable).

## HyCraft Mixins (Optional)

HyCraft Mixins is an optional module that uses [Hyxin](https://github.com/Build-9/Hyxin) to apply Mixin patches to Hytale's server internals. This enables deeper integration that isn't possible through the normal plugin API alone, such as intercepting interaction chains and profile lookups.

### How it works

Hytale loads early plugins before the server fully starts, allowing Hyxin to transform server classes at load time. HyCraft Mixins hooks into this process to modify specific server behaviors like interaction handling and player profile resolution.

Due to a classloader limitation in Hytale's `TransformingClassLoader`, mixin-injected code cannot directly reference classes from the plugin environment. Because of this, HyCraft Mixins communicates with the core plugin through a `System.getProperties()` bridge using `MethodHandle` to call hooks at runtime. This is the same approach used by other Hytale mixin plugins like OrbisGuard.

### Requirements

*   [Hyxin](https://github.com/Build-9/Hyxin) jar in the `earlyplugins/` folder.
*   HyCraft core plugin in the `mods/` folder (required dependency).

### Setup

1.  Download or build the `Hyxin.jar` and place it in your server's `earlyplugins/` folder.
2.  Download or build the `HyCraft-Mixins.jar` and place it in your server's `earlyplugins/` folder.
3.  Make sure the core `HyCraft` plugin is installed in `mods/` as usual.
4.  Start the server. You should see `HyCraft mixins loaded!` in the console.

### Classloader Limitation

Hytale's `TransformingClassLoader` only delegates to the platform classloader (JDK classes) and does not fall back to the app classloader where early plugin classes live. This means mixin-injected code cannot use `CallbackInfo.cancel()`, `CallbackInfoReturnable.setReturnValue()`, or reference any early plugin classes directly. All cross-classloader communication must go through JVM-global mechanisms like `System.getProperties()`. This is a known Hytale platform limitation, not a Hyxin or Mixin bug.

## Configuration

On the first run, a configuration file will be generated at `mods/HyCraft/main.json`.

| Option              | Description                                                                          | Default    |
|:--------------------|:-------------------------------------------------------------------------------------|:-----------|
| `port`              | The TCP port the Minecraft server will listen on.                                    | `25565`    |
| `player_prefix`     | A string prepended to the username of players connecting via Minecraft.              | `"."`      |
| `item_notification` | Format for action bar messages when receiving items.                                 | `...`      |
| `log_debug`         | Enables verbose debug logging to diagnose problems.                                  | `false`    |
| `server_icon`       | The MC server icon file path, relative to the HyCraft directory (must be 64x64 PNG). | `icon.png` |

## Building from Source

This project requires **Java 25** to build.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/EdwardBelt/HyCraft.git
    cd HyCraft
    ```
    
2.  **Build with Gradle:**
    ```bash
    # Linux/macOS
    ./gradlew shadowJar

    # Windows
    gradlew.bat shadowJar
    ```

3.  The compiled artifact will be located in `core/build/libs/`.

## Contributing

All contributions are welcome and highly appreciated. Feel free to open issues, suggest features, or submit pull requests.
## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

*   **Discord:** `@edwardbelt`
*   **Community:** [EdwardBelt Discord](https://discord.gg/6XTNSKQAAu)