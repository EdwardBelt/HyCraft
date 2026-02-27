package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetTitlePacket implements Packet {
    private String title;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeNBTStringTag(title);
    }
}
