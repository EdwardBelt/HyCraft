package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.player.ClientReady;
import com.hypixel.hytale.protocol.packets.player.JoinWorld;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class JoinWorldHandler implements PacketHandler<JoinWorld> {
    @Override
    public void handle(JoinWorld packet, ClientConnection connection) {
        connection.setNextWorldRespawn(packet.worldUuid);
        ClientReady hytaleClientReadyPacket = new ClientReady(true, false);
        connection.getHytaleChannel().sendPacket(hytaleClientReadyPacket);
    }
}
