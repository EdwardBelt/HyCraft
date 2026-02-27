package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.UnloadChunk;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.UnloadChunkPacket;

public class UnloadChunkHandler implements PacketHandler<UnloadChunk> {
    @Override
    public void handle(UnloadChunk packet, ClientConnection connection) {
        for (int dz = 0; dz < 2; dz++) {
            for (int dx = 0; dx < 2; dx++) {
                int newX = packet.chunkX * 2 + dx;
                int newZ = packet.chunkZ * 2 + dz;

                UnloadChunkPacket unloadPacket = new UnloadChunkPacket(newX, newZ);
                connection.getChannel().write(unloadPacket);
            }
        }

        connection.getChannel().flush();
    }
}
