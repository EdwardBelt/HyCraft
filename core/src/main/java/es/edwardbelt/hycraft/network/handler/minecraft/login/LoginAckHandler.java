package es.edwardbelt.hycraft.network.handler.minecraft.login;

import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.packet.login.LoginAcknowledgedPacket;

public class LoginAckHandler implements PacketHandler<LoginAcknowledgedPacket> {
    @Override
    public void handle(LoginAcknowledgedPacket packet, ClientConnection connection) {
        if (connection.getUuid() == null) {
            MinecraftServerBootstrap.get().disconnectConnection(connection, "Not logged in!");
            return;
        }
        connection.setState(ConnectionState.CONFIGURATION);
    }
}
