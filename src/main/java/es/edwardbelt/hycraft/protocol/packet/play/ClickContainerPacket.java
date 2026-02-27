package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class ClickContainerPacket implements Packet {
    private int windowId;
    private int stateId;
    private short slot;
    private byte button;
    private int mode;

    @Override
    public void read(PacketBuffer buffer) {
        this.windowId = buffer.readVarInt();
        this.stateId = buffer.readVarInt();
        this.slot = buffer.readShort();
        this.button = buffer.readByte();
        this.mode = buffer.readVarInt();
    }

    public Mode getClickMode() {
        return Mode.fromId(mode);
    }

    @Getter
    public enum Mode {
        NORMAL_CLICK(0),
        SHIFT_CLICK(1),
        NUMBER_KEY(2),
        MIDDLE_CLICK(3),
        DROP(4),
        DRAG(5),
        DOUBLE_CLICK(6);

        private final int id;

        Mode(int id) {
            this.id = id;
        }

        public static Mode fromId(int id) {
            for (Mode mode : values()) {
                if (mode.id == id) {
                    return mode;
                }
            }
            return NORMAL_CLICK;
        }
    }

    @Getter
    public enum Button {
        LEFT_CLICK(0),
        RIGHT_CLICK(1),

        SHIFT_LEFT_CLICK(0),
        SHIFT_RIGHT_CLICK(1),

        NUMBER_KEY_1(0),
        NUMBER_KEY_2(1),
        NUMBER_KEY_3(2),
        NUMBER_KEY_4(3),
        NUMBER_KEY_5(4),
        NUMBER_KEY_6(5),
        NUMBER_KEY_7(6),
        NUMBER_KEY_8(7),
        NUMBER_KEY_9(8),

        MIDDLE_CLICK(2),

        DROP_ITEM(0),
        DROP_STACK(1),

        START_LEFT_DRAG(0),
        ADD_SLOT_LEFT_DRAG(1),
        END_LEFT_DRAG(2),
        START_RIGHT_DRAG(4),
        ADD_SLOT_RIGHT_DRAG(5),
        END_RIGHT_DRAG(6),
        START_MIDDLE_DRAG(8),
        ADD_SLOT_MIDDLE_DRAG(9),
        END_MIDDLE_DRAG(10),

        DOUBLE_CLICK(0),
        PICKUP_ALL_REVERSE(1);

        private final int id;

        Button(int id) {
            this.id = id;
        }

    }
}