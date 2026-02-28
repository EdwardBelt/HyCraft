package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class KeepAlivePacket implements Packet {
    private final long keepAliveId;

    public KeepAlivePacket(long keepAliveId) {
        this.keepAliveId = keepAliveId;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(keepAliveId);
    }
}