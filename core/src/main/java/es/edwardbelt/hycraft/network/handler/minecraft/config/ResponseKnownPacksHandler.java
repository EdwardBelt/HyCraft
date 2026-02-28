package es.edwardbelt.hycraft.network.handler.minecraft.config;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.configuration.FinishConfigurationPacket;
import es.edwardbelt.hycraft.protocol.packet.configuration.RegistryDataPacket;
import es.edwardbelt.hycraft.protocol.packet.configuration.ResponseKnownPacksPacket;

public class ResponseKnownPacksHandler implements PacketHandler<ResponseKnownPacksPacket> {
    @Override
    public void handle(ResponseKnownPacksPacket packet, ClientConnection connection) {
        for (RegistryDataPacket registryPacket : RegistryDataPacket.DEFAULT_REGISTRIES) {
            connection.getChannel().writeAndFlush(registryPacket);
        }
        connection.getChannel().writeAndFlush(new FinishConfigurationPacket());
    }
}
