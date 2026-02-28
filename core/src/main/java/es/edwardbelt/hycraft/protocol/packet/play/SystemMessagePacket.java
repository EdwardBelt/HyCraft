package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SystemMessagePacket implements Packet {
    private String message;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeNBTStringTag(message);
        buffer.writeBoolean(false);
    }
}
