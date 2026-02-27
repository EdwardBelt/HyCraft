package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class ClientCommandPacket implements Packet {
    private int action;

    @Override
    public void read(PacketBuffer buffer) {
        this.action = buffer.readVarInt();
    }
}
