package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetTitleAnimationPacket implements Packet {
    private int fadeIn;
    private int stay;
    private int fadeOut;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(fadeIn);
        buffer.writeInt(stay);
        buffer.writeInt(fadeOut);
    }
}
