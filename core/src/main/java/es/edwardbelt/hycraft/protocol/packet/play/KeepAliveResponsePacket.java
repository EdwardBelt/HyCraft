package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class KeepAliveResponsePacket implements Packet {
    private long keepAliveId;

    @Override
    public void read(PacketBuffer buffer) {
        this.keepAliveId = buffer.readLong();
    }

    public long getKeepAliveId() {
        return keepAliveId;
    }
}