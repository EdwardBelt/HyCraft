package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class SetCarriedSlotPacket implements Packet {
    private short slotId;

    @Override
    public void read(PacketBuffer buffer) {
        this.slotId = buffer.readShort();
    }
}
