package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class MoveEntityPacket implements Packet {
    private int entityId;
    private short deltaX;
    private short deltaY;
    private short deltaZ;
    private boolean onGround;

    public MoveEntityPacket(int entityId, double deltaX, double deltaY, double deltaZ, boolean onGround) {
        this.entityId = entityId;
        this.deltaX = (short) (deltaX * 4096);
        this.deltaY = (short) (deltaY * 4096);
        this.deltaZ = (short) (deltaZ * 4096);
        this.onGround = onGround;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeShort(deltaX);
        buffer.writeShort(deltaY);
        buffer.writeShort(deltaZ);
        buffer.writeBoolean(onGround);
    }
}
