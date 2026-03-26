package es.edwardbelt.hycraft.network.handler.hytale.connection;

import com.hypixel.hytale.protocol.packets.connection.ClientDisconnect;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class DisconnectHandler implements PacketHandler<ClientDisconnect> {
    @Override
    public void handle(ClientDisconnect packet, ClientConnection connection) {
        /*System.out.println("received disconnect packet");
        System.out.println(packet.reason);
        System.out.println(packet.type.getValue());*/
    }
}
