package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EntityAnimationPacket implements Packet {
    private int entityId;
    private Animation animation;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUnsignedByte(animation.getId());
    }

    @AllArgsConstructor
    @Getter
    public enum Animation {
        SWING_HAND((byte) 0),
        LEAVE_BED((byte) 2),
        SWING_OFFHAND((byte) 3),
        CRITICAL((byte) 4),
        MAGIC_CRITICAL((byte) 5);

        private final byte id;
    }
}
