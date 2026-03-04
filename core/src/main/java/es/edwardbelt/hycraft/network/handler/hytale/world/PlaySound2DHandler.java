package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.PlaySoundEvent2D;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.EntitySoundPacket;
import es.edwardbelt.hycraft.util.Logger;

public class PlaySound2DHandler implements PacketHandler<PlaySoundEvent2D> {
    @Override
    public void handle(PlaySoundEvent2D packet, ClientConnection connection) {
        int soundId = MappingRegistry.get().getSoundMapper().getMapping(packet.soundEventIndex);
        if (soundId == -1) return;
        Logger.DEBUG.log("Playing 2D sound " + soundId + " for " + connection.getUsername());
        EntitySoundPacket soundPacket = new EntitySoundPacket(soundId, 7, connection.getNetworkId(), packet.volumeModifier, packet.pitchModifier, 0);
        connection.getChannel().writeAndFlush(soundPacket);
    }
}
