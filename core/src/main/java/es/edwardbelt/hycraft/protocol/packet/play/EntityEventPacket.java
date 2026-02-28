package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityEventPacket implements Packet {
    private int entityId;
    private int event;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeByte((byte) event);
    }
}
