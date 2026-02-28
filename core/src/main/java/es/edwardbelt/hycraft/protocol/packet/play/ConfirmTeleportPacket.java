package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class ConfirmTeleportPacket implements Packet {
    private int id;

    @Override
    public void read(PacketBuffer buffer) {
        this.id = buffer.readVarInt();
    }
}
