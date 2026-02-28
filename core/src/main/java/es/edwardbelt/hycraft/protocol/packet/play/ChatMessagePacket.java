package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class ChatMessagePacket implements Packet {
    private String message;
    private long timestamp;

    @Override
    public void read(PacketBuffer buffer) {
        this.message = buffer.readString();
        this.timestamp = buffer.readLong();
    }
}
