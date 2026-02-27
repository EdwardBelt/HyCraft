package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class PlayerActionPacket implements Packet {
    private Status status;
    private BlockPosition position;
    private Face face;
    private int sequence;

    @Override
    public void read(PacketBuffer buffer) {
        this.status = Status.fromId(buffer.readVarInt());
        this.position = BlockPosition.fromLong(buffer.readLong());
        this.face = Face.fromId(buffer.readByte());
        this.sequence = buffer.readVarInt();
    }

    public enum Status {
        STARTED_DIGGING(0),
        CANCELLED_DIGGING(1),
        FINISHED_DIGGING(2),
        DROP_ITEM_STACK(3),
        DROP_ITEM(4),
        SHOOT_ARROW_FINISH_EATING(5),
        SWAP_ITEM_IN_HAND(6);

        private final int id;

        Status(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Status fromId(int id) {
            for (Status status : values()) {
                if (status.id == id) {
                    return status;
                }
            }
            return null;
        }
    }

    public enum Face {
        BOTTOM(0),
        TOP(1),
        NORTH(2),
        SOUTH(3),
        WEST(4),
        EAST(5);

        private final int id;

        Face(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Face fromId(int id) {
            for (Face face : values()) {
                if (face.id == id) {
                    return face;
                }
            }
            return null;
        }
    }

}