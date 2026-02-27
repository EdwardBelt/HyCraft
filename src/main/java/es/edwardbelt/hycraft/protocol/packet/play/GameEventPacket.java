package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameEventPacket implements Packet {
    private int event;
    private float value;

    public GameEventPacket(int event, float value) {
        this.event = event;
        this.value = value;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUnsignedByte(event);
        buffer.writeFloat(value);
    }
}