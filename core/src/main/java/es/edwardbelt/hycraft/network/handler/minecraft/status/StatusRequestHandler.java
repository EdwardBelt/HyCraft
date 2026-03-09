package es.edwardbelt.hycraft.network.handler.minecraft.status;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.Universe;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.StatusResponse;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import es.edwardbelt.hycraft.protocol.packet.status.StatusRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.StatusResponsePacket;
import es.edwardbelt.hycraft.util.ServerIconUtil;

public class StatusRequestHandler implements PacketHandler<StatusRequestPacket> {
    @Override
    public void handle(StatusRequestPacket packet, ClientConnection connection) {
        StatusResponse response = new StatusResponse(
                ProtocolConstants.MINECRAFT_VERSION,
                ProtocolConstants.PROTOCOL_VERSION,
                HytaleServer.get().getConfig().getMaxPlayers(),
                Universe.get().getPlayerCount(),
                HytaleServer.get().getConfig().getMotd(),
                "data:image/png;base64," + ServerIconUtil.getServerIconBase64()
        );
        StatusResponsePacket responsePacket = new StatusResponsePacket(response);
        connection.getChannel().writeAndFlush(responsePacket);
    }
}
