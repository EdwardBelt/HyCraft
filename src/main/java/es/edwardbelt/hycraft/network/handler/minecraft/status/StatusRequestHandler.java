package es.edwardbelt.hycraft.network.handler.minecraft.status;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.Universe;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.StatusResponse;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.status.StatusRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.StatusResponsePacket;

public class StatusRequestHandler implements PacketHandler<StatusRequestPacket> {
    @Override
    public void handle(StatusRequestPacket packet, ClientConnection connection) {
        StatusResponse response = new StatusResponse(
                "1.21.10",
                773,
                HytaleServer.get().getConfig().getMaxPlayers(),
                Universe.get().getPlayerCount(),
                HytaleServer.get().getConfig().getMotd()
        );
        StatusResponsePacket responsePacket = new StatusResponsePacket(response);
        connection.getChannel().writeAndFlush(responsePacket);
    }
}
