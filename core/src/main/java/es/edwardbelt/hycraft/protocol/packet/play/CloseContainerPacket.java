package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CloseContainerPacket implements Packet {
    private int windowId;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(windowId);
    }
}
