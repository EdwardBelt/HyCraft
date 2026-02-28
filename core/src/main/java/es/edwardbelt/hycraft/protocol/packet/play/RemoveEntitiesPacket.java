package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveEntitiesPacket implements Packet {
    private int[] entityIds;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityIds.length);
        for (int id : entityIds) buffer.writeVarInt(id);
    }
}
