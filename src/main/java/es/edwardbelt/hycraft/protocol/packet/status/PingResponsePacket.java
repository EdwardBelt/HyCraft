package es.edwardbelt.hycraft.protocol.packet.status;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PingResponsePacket implements Packet {
    private long timestamp;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(timestamp);
    }
}
