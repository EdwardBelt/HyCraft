package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnloadChunkPacket implements Packet {
    private int x;
    private int z;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(z);
        buffer.writeInt(x);
    }
}
