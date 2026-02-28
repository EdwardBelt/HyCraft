package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetHeldSlotPacket implements Packet {
    private int slot;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(slot);
    }
}
