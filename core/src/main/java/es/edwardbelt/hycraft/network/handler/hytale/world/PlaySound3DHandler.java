package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.PlaySoundEvent3D;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SoundPacket;
import es.edwardbelt.hycraft.util.Logger;

public class PlaySound3DHandler implements PacketHandler<PlaySoundEvent3D> {
    @Override
    public void handle(PlaySoundEvent3D packet, ClientConnection connection) {
        int soundId = MappingRegistry.get().getSoundMapper().getMapping(packet.soundEventIndex);
        if (soundId == -1) return;
        Logger.DEBUG.log("Playing 3D sound " + soundId + " for " + connection.getUsername());
        SoundPacket soundPacket = new SoundPacket(soundId, 7, packet.position.x, packet.position.y, packet.position.z, packet.volumeModifier, packet.pitchModifier, 0);
        connection.getChannel().writeAndFlush(soundPacket);
    }
}
