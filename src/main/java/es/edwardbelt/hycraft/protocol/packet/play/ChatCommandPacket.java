package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class ChatCommandPacket implements Packet {
    private String command;

    @Override
    public void read(PacketBuffer buffer) {
        command = buffer.readString();
    }
}
