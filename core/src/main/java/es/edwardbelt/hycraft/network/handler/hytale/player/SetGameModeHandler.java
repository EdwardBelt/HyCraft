package es.edwardbelt.hycraft.network.handler.hytale.player;

import com.hypixel.hytale.protocol.packets.player.SetGameMode;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.GameEventPacket;

public class SetGameModeHandler implements PacketHandler<SetGameMode> {
    @Override
    public void handle(SetGameMode packet, ClientConnection connection) {
        GameEventPacket gameEventPacket = new GameEventPacket(3, packet.gameMode.getValue());
        connection.getChannel().writeAndFlush(gameEventPacket);
    }
}
