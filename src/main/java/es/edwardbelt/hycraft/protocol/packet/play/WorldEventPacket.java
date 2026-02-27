package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorldEventPacket implements Packet {
    private int event;
    private BlockPosition position;
    private int data;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(event);
        buffer.writeLong(position.toLong());
        buffer.writeInt(data);
        buffer.writeBoolean(false);
    }
}
