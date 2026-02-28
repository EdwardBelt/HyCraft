package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.packets.interface_.RemoveFromServerPlayerList;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerInfoRemovePacket;

import java.util.Arrays;

public class RemoveFromServerPlayerListHandler implements PacketHandler<RemoveFromServerPlayerList> {
    @Override
    public void handle(RemoveFromServerPlayerList packet, ClientConnection connection) {
        PlayerInfoRemovePacket removePacket = new PlayerInfoRemovePacket(Arrays.stream(packet.players).toList());
        connection.getChannel().writeAndFlush(removePacket);
    }
}
