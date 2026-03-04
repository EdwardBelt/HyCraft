package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.api.gui.HyCraftClickType;
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
}