package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgeBlockChangePacket implements Packet {
    private int sequence;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(sequence);
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.sequence = buffer.readVarInt();
    }
}