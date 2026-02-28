package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class ServerDataPacket implements Packet {
    private final String motd;
    private final byte[] icon;

    public ServerDataPacket(String motd, byte[] icon) {
        this.motd = motd;
        this.icon = icon;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(motd);
        buffer.writeBoolean(icon != null);
        if (icon != null) {
            buffer.writeVarInt(icon.length);
            buffer.writeBytes(icon);
        }
    }
}