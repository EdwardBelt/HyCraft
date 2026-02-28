package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.packets.interface_.UpdateServerPlayerListPing;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerInfoUpdatePacket;

import java.util.ArrayList;
import java.util.List;

public class UpdateServerPlayerListPingHandler implements PacketHandler<UpdateServerPlayerListPing> {
    @Override
    public void handle(UpdateServerPlayerListPing packet, ClientConnection connection) {
        assert packet.players != null;
        List<PlayerInfoUpdatePacket.PlayerInfo> playerInfos = new ArrayList<>();
        packet.players.forEach((uuid, ping) -> {
            PlayerInfoUpdatePacket.PlayerInfo playerInfo = new PlayerInfoUpdatePacket.PlayerInfo(uuid);
            playerInfo.ping = ping;

            playerInfos.add(playerInfo);
        });

        PlayerInfoUpdatePacket addPlayerPacket = new PlayerInfoUpdatePacket(
                new PlayerInfoUpdatePacket.Actions().updateLatency().build(),
                playerInfos
        );

        connection.getChannel().writeAndFlush(addPlayerPacket);
    }
}
