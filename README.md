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

## Configuration

On the first run, a configuration file will be generated at `mods/HyCraft/main.json`.

```json
{
  "port": 25565,
  "player_prefix": ".",
  "item_notification": "&a+ {quantity} {item}",
  "log_debug": false
}
```

| Option              | Description                                                             | Default |
|:--------------------|:------------------------------------------------------------------------|:--------|
| `port`              | The TCP port the Minecraft server will listen on.                       | `25565` |
| `player_prefix`     | A string prepended to the username of players connecting via Minecraft. | `"."`   |
| `item_notification` | Format for action bar messages when receiving items.                    | `...`   |
| `log_debug`         | Enables verbose debug logging to diagnose problems.                     | `false` |

## Building from Source

This project requires **Java 25** to build.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/EdwardBelt/HyCraft.git
    cd HyCraft
    ```

2.  **Provide Dependencies:**
    You must provide a valid `HytaleServer.jar` for compilation.
    *   Create a `lib/` directory in the project root.
    *   Copy your `HytaleServer.jar` into `lib/`.

3.  **Build with Gradle:**
    ```bash
    # Linux/macOS
    ./gradlew shadowJar

    # Windows
    gradlew.bat shadowJar
    ```

4.  The compiled artifact will be located in `core/build/libs/`.

## Contributing

All contributions are welcome and highly appreciated. Feel free to open issues, suggest features, or submit pull requests.
## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

*   **Discord:** `@edwardbelt`
*   **Community:** [EdwardBelt Discord](https://discord.gg/6XTNSKQAAu)