package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.SpawnParticleSystem;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.util.Logger;

public class SpawnParticleHandler implements PacketHandler<SpawnParticleSystem> {
    @Override
    public void handle(SpawnParticleSystem packet, ClientConnection connection) {
        Logger.DEBUG.log("Spawning particle " + packet.particleSystemId + " for " + connection.getUsername());
    }
}
