package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.PlaySoundEvent3D;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SoundPacket;

public class PlaySound3DHandler implements PacketHandler<PlaySoundEvent3D> {
    @Override
    public void handle(PlaySoundEvent3D packet, ClientConnection connection) {
        /*System.out.println("received 3d sound: " + packet.soundEventIndex);
        System.out.println(SoundEvent.getAssetMap().getAsset(packet.soundEventIndex).getId());
        System.out.println();*/
        int soundId = MappingRegistry.get().getSoundMapper().getMapping(packet.soundEventIndex);
        if (soundId == -1) return;
        SoundPacket soundPacket = new SoundPacket(soundId, 7, packet.position.x, packet.position.y, packet.position.z, packet.volumeModifier, packet.pitchModifier, 0);
        connection.getChannel().writeAndFlush(soundPacket);
    }
}
