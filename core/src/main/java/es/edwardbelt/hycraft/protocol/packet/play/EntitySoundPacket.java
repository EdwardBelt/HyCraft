package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntitySoundPacket implements Packet {
    private int id;
    private int categoryId;
    private int entityId;
    private float volume;
    private float pitch;
    private long seed;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(id+1);
        buffer.writeVarInt(categoryId);
        buffer.writeVarInt(entityId);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
        buffer.writeLong(seed);
    }
}
