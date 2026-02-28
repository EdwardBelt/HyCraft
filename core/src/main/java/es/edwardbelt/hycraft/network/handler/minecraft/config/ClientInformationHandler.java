package es.edwardbelt.hycraft.network.handler.minecraft.config;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import es.edwardbelt.hycraft.protocol.packet.configuration.ClientInformationPacket;
import es.edwardbelt.hycraft.protocol.packet.configuration.SendKnownPacksPacket;

public class ClientInformationHandler implements PacketHandler<ClientInformationPacket> {
    @Override
    public void handle(ClientInformationPacket packet, ClientConnection connection) {
        connection.setViewDistance(Math.min(packet.getViewDistance(), 16));
        SendKnownPacksPacket sendKnownPacksPacket = new SendKnownPacksPacket("minecraft", "core", ProtocolConstants.MINECRAFT_VERSION);
        connection.getChannel().writeAndFlush(sendKnownPacksPacket);
    }
}
