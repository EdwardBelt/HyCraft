package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.SpawnParticleSystem;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class SpawnParticleHandler implements PacketHandler<SpawnParticleSystem> {
    @Override
    public void handle(SpawnParticleSystem packet, ClientConnection connection) {
        /*System.out.println("particle");
        System.out.println("id: " + packet.particleSystemId);
        System.out.println();*/
    }
}
