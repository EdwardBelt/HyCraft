# HyCraft

HyCraft is a Hytale mod that bridges Minecraft 1.21.10 clients to Hytale servers via packet translation. It spins up a TCP server that intercepts incoming Minecraft packets, translates them, and forwards them to the underlying Hytale server, allowing Minecraft players to connect without any client-side modifications.

### Test Server: 45.59.171.227:25565

> ⚠️ This project is in early development. Expect bugs and incomplete features.

## How it works

Minecraft communicates over TCP, so HyCraft starts a TCP listener that sits between the Minecraft client and the Hytale server. Packets are translated on the fly from the Minecraft protocol to the Hytale protocol and forwarded transparently.

## Roadmap

- Add a logging system
- Make swords also work as shields
- Improve all mappings
- Refactor inventory manager
- Make chest interfaces work with Minecraft guis
- Texture pack generator

## Contributing

All contributions are welcome and highly appreciated. Feel free to open issues, suggest features, or submit pull requests.

## Contact

Have questions or want to get involved? Reach out on Discord:

- `@edwardbelt`
- [Join the Discord server](https://discord.gg/6XTNSKQAAu)