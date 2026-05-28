package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import org.joml.Vector3f;

@AllArgsConstructor
public class SetEntityVelocityPacket implements Packet {
    private int entityId;
    private Vector3f velocity;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeLpVec3(hytaleVelocityToMinecraft(velocity.x), hytaleVelocityToMinecraft(velocity.y), hytaleVelocityToMinecraft(velocity.z));
    }

    private static double hytaleVelocityToMinecraft(double hytaleBlocksPerSecond) {
        return hytaleBlocksPerSecond / 30;
    }
}
