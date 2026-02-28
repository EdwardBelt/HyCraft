package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class MoveAndRotEntityPacket implements Packet {
    private int entityId;
    private short deltaX;
    private short deltaY;
    private short deltaZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public MoveAndRotEntityPacket(int entityId, double deltaX, double deltaY, double deltaZ, float yaw, float pitch, boolean onGround) {
        this.entityId = entityId;
        this.deltaX = (short) (deltaX * 4096);
        this.deltaY = (short) (deltaY * 4096);
        this.deltaZ = (short) (deltaZ * 4096);
        this.yaw = (byte) (yaw * 256.0F / 360.0F);
        this.pitch = (byte) (pitch * 256.0F / 360.0F);
        this.onGround = onGround;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeShort(deltaX);
        buffer.writeShort(deltaY);
        buffer.writeShort(deltaZ);
        buffer.writeByte(yaw);
        buffer.writeByte(pitch);
        buffer.writeBoolean(onGround);
    }
}
