package es.edwardbelt.hycraft.network.handler.hytale.connection;

import com.hypixel.hytale.protocol.packets.player.SetClientId;
import com.hypixel.hytale.server.core.universe.Universe;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.LoginPlayPacket;

public class SetClientIdHandler implements PacketHandler<SetClientId> {
    @Override
    public void handle(SetClientId packet, ClientConnection connection) {
        connection.setNetworkId(packet.clientId);
        if (connection.isInitialized()) return;

        LoginPlayPacket loginPacket = getLoginPlayPacket(connection, HytaleUtil.getDimensionName(connection.getNextWorldRespawn()));
        connection.getChannel().writeAndFlush(loginPacket);
        connection.setNextWorldRespawn(null);
    }

    private LoginPlayPacket getLoginPlayPacket(ClientConnection connection, String dimension) {
        String[] dimensions = Universe.get().getWorlds().keySet().toArray(new String[0]);

        return new LoginPlayPacket(
                connection.getNetworkId(),
                false,
                dimensions,
                0,
                32,
                32,
                false,
                false,
                false,
                0,
                dimension,
                0,
                0,
                (byte) -1,
                false,
                false,
                false,
                null,
                0,
                0,
                0,
                false
        );
    }
}
