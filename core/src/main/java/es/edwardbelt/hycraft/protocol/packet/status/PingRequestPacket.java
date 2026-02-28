package es.edwardbelt.hycraft.protocol.packet.status;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PingRequestPacket implements Packet {
    @Getter
    private long timestamp;

    @Override
    public void read(PacketBuffer buffer) {
        this.timestamp = buffer.readLong();
    }
}
