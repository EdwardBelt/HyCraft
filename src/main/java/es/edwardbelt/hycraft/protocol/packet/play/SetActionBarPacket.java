package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetActionBarPacket implements Packet {
    private String text;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeNBTStringTag(text);
    }
}
