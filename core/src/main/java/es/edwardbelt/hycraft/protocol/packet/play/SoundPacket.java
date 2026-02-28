package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SoundPacket implements Packet {
    private int soundId;
    private int categoryId;
    private double x;
    private double y;
    private double z;
    private float volume;
    private float pitch;
    private long seed;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(soundId+1);
        buffer.writeVarInt(categoryId);
        buffer.writeInt((int)(x * 8.0));
        buffer.writeInt((int)(y * 8.0));
        buffer.writeInt((int)(z * 8.0));
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
        buffer.writeLong(seed);
    }
}