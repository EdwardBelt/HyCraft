package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class RotateEntityPacket implements Packet {
    private int entityId;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public RotateEntityPacket(int entityId, float yaw, float pitch, boolean onGround) {
        this.entityId = entityId;
        this.yaw = (byte) (yaw * 256.0F / 360.0F);
        this.pitch = (byte) (pitch * 256.0F / 360.0F);
        this.onGround = onGround;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeByte(yaw);
        buffer.writeByte(pitch);
        buffer.writeBoolean(onGround);
    }
}
