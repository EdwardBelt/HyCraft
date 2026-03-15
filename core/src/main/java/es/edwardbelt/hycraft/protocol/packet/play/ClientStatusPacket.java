package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class ClientStatusPacket implements Packet {
    private int actionId;

    @Override
    public void read(PacketBuffer buffer) {
        try {
            int readerIndex = buffer.getBuffer().readerIndex();
            int readableBytes = buffer.getBuffer().readableBytes();
            this.actionId = buffer.readVarInt();
        } catch (RuntimeException e) {
            System.err.println("[VarInt Error] ClientStatusPacket decode failed:");
            System.err.println("  Reader index: " + buffer.getBuffer().readerIndex());
            System.err.println("  Readable bytes: " + buffer.getBuffer().readableBytes());
            System.err.println("  Buffer capacity: " + buffer.getBuffer().capacity());
            throw e;
        }
    }

    public int getActionId() {
        return actionId;
    }
}
