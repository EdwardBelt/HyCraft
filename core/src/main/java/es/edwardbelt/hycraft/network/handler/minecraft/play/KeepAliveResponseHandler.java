package es.edwardbelt.hycraft.network.handler.minecraft.play;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.KeepAliveResponsePacket;

public class KeepAliveResponseHandler implements PacketHandler<KeepAliveResponsePacket> {
    @Override
    public void handle(KeepAliveResponsePacket packet, ClientConnection connection) {
        /*Ping pendingPing = connection.getPendingPing();
        if (pendingPing != null) {
            for (PongType type : PongType.VALUES) {
                Pong pongPacket = new Pong(pendingPing.id, pendingPing.time, type, (short) 0);
                connection.getHytaleChannel().sendPacket(pongPacket);
            }
            connection.setPendingPing(null);
        }*/
    }
}
