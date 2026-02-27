package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class EntityInteractPacket implements Packet {
    private int entityId;
    private Type type;
    private float targetX;
    private float targetY;
    private float targetZ;
    private int hand;
    private boolean isSneakKeyPressed;

    @Override
    public void read(PacketBuffer buffer) {
        this.entityId = buffer.readVarInt();
        this.type = Type.fromId(buffer.readVarInt());

        if (this.type.equals(Type.INTERACT_AT)) {
            this.targetX = buffer.readFloat();
            this.targetY = buffer.readFloat();
            this.targetZ = buffer.readFloat();
        }

        if (!this.type.equals(Type.ATTACK)) this.hand = buffer.readVarInt();

        this.isSneakKeyPressed = buffer.readBoolean();
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        INTERACT(0),
        ATTACK(1),
        INTERACT_AT(2);

        private final int id;

        public static Type fromId(int id) {
            return switch (id) {
                case 0 -> INTERACT;
                case 1 -> ATTACK;
                case 2 -> INTERACT_AT;
                default -> INTERACT;
            };
        }
    }
}
