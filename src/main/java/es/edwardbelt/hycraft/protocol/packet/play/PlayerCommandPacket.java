package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class PlayerCommandPacket implements Packet {
    private int entityId;
    private Action action;
    private int jumpBoost;

    @Override
    public void read(PacketBuffer buffer) {
        this.entityId = buffer.readVarInt();
        int actionId = buffer.readVarInt();
        this.action = Action.fromId(actionId);
        this.jumpBoost = buffer.readVarInt();
    }

    @Getter
    public enum Action {
        LEAVE_BED(0),
        START_SPRINTING(1),
        STOP_SPRINTING(2),
        START_JUMP_WITH_HORSE(3),
        STOP_JUMP_WITH_HORSE(4),
        OPEN_VEHICLE_INVENTORY(5),
        START_FLYING_WITH_ELYTRA(6);

        private final int id;

        Action(int id) {
            this.id = id;
        }

        public static Action fromId(int id) {
            for (Action action : values()) {
                if (action.id == id) {
                    return action;
                }
            }
            return null;
        }
    }
}