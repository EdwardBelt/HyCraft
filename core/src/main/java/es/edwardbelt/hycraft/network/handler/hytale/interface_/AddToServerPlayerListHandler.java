package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.packets.interface_.AddToServerPlayerList;
import com.hypixel.hytale.protocol.packets.interface_.ServerPlayerListPlayer;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerInfoUpdatePacket;

import java.util.*;

public class AddToServerPlayerListHandler implements PacketHandler<AddToServerPlayerList> {
    @Override
    public void handle(AddToServerPlayerList packet, ClientConnection connection) {
        assert packet.players != null;
        System.out.println("received player list packet");
        List<PlayerInfoUpdatePacket.PlayerInfo> playerInfos = new ArrayList<>();
        for (ServerPlayerListPlayer player : packet.players) {
            PlayerInfoUpdatePacket.PlayerInfo playerInfo = new PlayerInfoUpdatePacket.PlayerInfo(player.uuid);
            playerInfo.listed = true;
            playerInfo.ping = player.ping;
            playerInfo.name = sanitizeName(player.username);
            playerInfo.displayName = player.username;

            ClientConnection playerConnection = MinecraftServerBootstrap.get().getConnection(player.uuid);
            if (playerConnection != null) {
                playerInfo.properties = playerConnection.getProfile().getProperties();
            } else {
                playerInfo.properties = new ArrayList<>();
            }

            playerInfos.add(playerInfo);
        }

        PlayerInfoUpdatePacket addPlayerPacket = new PlayerInfoUpdatePacket(
                new PlayerInfoUpdatePacket.Actions().addPlayer().updateListed().updateLatency().updateDisplayName().build(),
                playerInfos
        );

        connection.getChannel().writeAndFlush(addPlayerPacket);
    }

    private String sanitizeName(String name) {
        if (name == null) return "";

        int space = name.lastIndexOf(' ');
        if (space != -1) {
            name = name.substring(space + 1);
        }

        if (name.length() > 16) {
            name = name.substring(0, 16);
        }

        return name;
    }
}
