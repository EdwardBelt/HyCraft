package es.edwardbelt.hycraft.network.handler.minecraft.status;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.status.PingRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.PingResponsePacket;

public class PingRequestHandler implements PacketHandler<PingRequestPacket> {
    @Override
    public void handle(PingRequestPacket packet, ClientConnection connection) {
        connection.getChannel().writeAndFlush(new PingResponsePacket(packet.getTimestamp()));
    }
}
