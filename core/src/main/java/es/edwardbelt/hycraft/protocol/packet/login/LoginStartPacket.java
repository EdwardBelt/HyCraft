package es.edwardbelt.hycraft.protocol.packet.login;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class LoginStartPacket implements Packet {
    private String name;
    private UUID uuid;

    @Override
    public void read(PacketBuffer buffer) {
        this.name = buffer.readString(16);
        this.uuid = buffer.readUUID();
    }
}
