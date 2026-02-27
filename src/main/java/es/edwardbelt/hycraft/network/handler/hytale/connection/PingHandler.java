package es.edwardbelt.hycraft.network.handler.hytale.connection;

import com.hypixel.hytale.protocol.packets.connection.Ping;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.KeepAlivePacket;

public class PingHandler implements PacketHandler<Ping> {
    @Override
    public void handle(Ping packet, ClientConnection connection) {
        connection.setPendingPing(packet);

        KeepAlivePacket keepAlivePacket = new KeepAlivePacket(System.currentTimeMillis());
        connection.getChannel().writeAndFlush(keepAlivePacket);
    }
}