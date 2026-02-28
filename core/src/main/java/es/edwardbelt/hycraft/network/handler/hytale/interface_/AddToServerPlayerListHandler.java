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
        List<PlayerInfoUpdatePacket.PlayerInfo> playerInfos = new ArrayList<>();
        for (ServerPlayerListPlayer player : packet.players) {
            PlayerInfoUpdatePacket.PlayerInfo playerInfo = new PlayerInfoUpdatePacket.PlayerInfo(player.uuid);
            playerInfo.listed = true;
            playerInfo.ping = player.ping;
            playerInfo.name = player.username;
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
}
