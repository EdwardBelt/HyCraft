package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.UpdateTime;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.UpdateTimePacket;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UpdateTimeHandler implements PacketHandler<UpdateTime> {
    @Override
    public void handle(UpdateTime packet, ClientConnection connection) {
        if (packet.gameTime == null) return;

        long seconds = packet.gameTime.seconds;
        long secondsOfDay = ((seconds % 86400) + 86400) % 86400;
        long timeOfDay = ((secondsOfDay * 24000 / 86400) + 18000) % 24000;

        UpdateTimePacket timePacket = new UpdateTimePacket(0, timeOfDay, false);
        connection.getChannel().writeAndFlush(timePacket);
    }
}
