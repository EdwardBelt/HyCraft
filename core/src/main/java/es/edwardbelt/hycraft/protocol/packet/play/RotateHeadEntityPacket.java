package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class RotateHeadEntityPacket implements Packet {
    private int entityId;
    private byte headYaw;

    public RotateHeadEntityPacket(int entityId, float headYaw) {
        this.entityId = entityId;
        this.headYaw = (byte) (headYaw * 256.0F / 360.0F);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeByte(headYaw);
    }
}
