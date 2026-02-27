package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetCooldownPacket implements Packet {
    private String identifier;
    private int ticks;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(identifier);
        buffer.writeVarInt(ticks);
    }
}
