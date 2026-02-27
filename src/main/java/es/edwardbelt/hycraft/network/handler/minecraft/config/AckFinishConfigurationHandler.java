package es.edwardbelt.hycraft.network.handler.minecraft.config;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.packet.configuration.AckFinishConfigurationPacket;

public class AckFinishConfigurationHandler implements PacketHandler<AckFinishConfigurationPacket> {
    @Override
    public void handle(AckFinishConfigurationPacket packet, ClientConnection connection) {
        connection.setState(ConnectionState.PLAY);
        HytaleUtil.createPlayer(connection);
    }
}
