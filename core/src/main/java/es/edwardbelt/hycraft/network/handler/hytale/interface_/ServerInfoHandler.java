package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.Asset;
import com.hypixel.hytale.protocol.packets.interface_.ServerInfo;
import com.hypixel.hytale.protocol.packets.setup.PlayerOptions;
import com.hypixel.hytale.protocol.packets.setup.RequestAssets;
import com.hypixel.hytale.protocol.packets.setup.ViewRadius;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class ServerInfoHandler implements PacketHandler<ServerInfo> {
    @Override
    public void handle(ServerInfo packet, ClientConnection connection) {
        System.out.println("received serverinfo packet");
        System.out.println("name: " + packet.serverName);
        System.out.println("motd: " + packet.motd);
        System.out.println("max players: " + packet.maxPlayers);

        RequestAssets requestAssetsPacket = new RequestAssets(new Asset[0]);
        connection.getHytaleChannel().sendPacket(requestAssetsPacket);

        ViewRadius viewRadiusPacket = new ViewRadius(connection.getViewDistance()*16);
        connection.getHytaleChannel().sendPacket(viewRadiusPacket);

        PlayerOptions playerOptionsPacket = new PlayerOptions();
        connection.getHytaleChannel().sendPacket(playerOptionsPacket);
    }
}
