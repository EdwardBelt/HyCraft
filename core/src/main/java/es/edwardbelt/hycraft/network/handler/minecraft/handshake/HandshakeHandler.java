package es.edwardbelt.hycraft.network.handler.minecraft.handshake;

import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import es.edwardbelt.hycraft.protocol.packet.handshake.HandshakePacket;

public class HandshakeHandler implements PacketHandler<HandshakePacket> {
    @Override
    public void handle(HandshakePacket packet, ClientConnection connection) {
        ConnectionState state = packet.getConnectionState();
        int protocol = packet.getProtocolVersion();

        if (!state.equals(ConnectionState.STATUS)) {
            if (protocol != ProtocolConstants.PROTOCOL_VERSION) {
                MinecraftServerBootstrap.get().disconnectConnection(connection, "You must use " + ProtocolConstants.MINECRAFT_VERSION + " version!");
                return;
            }
        }

        if (state.equals(ConnectionState.CONFIGURATION) || state.equals(ConnectionState.PLAY)) {
            MinecraftServerBootstrap.get().disconnectConnection(connection, "Invalid intent!");
            return;
        }

        connection.setProtocolVersion(protocol);
        connection.setState(state);
    }
}
