package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class PlayerInputPacket implements Packet {
    private int flags;

    @Override
    public void read(PacketBuffer buffer) {
        this.flags = buffer.readUnsignedByte();
    }

    public boolean isForward() {
        return (flags & 0x01) != 0;
    }

    public boolean isBackward() {
        return (flags & 0x02) != 0;
    }

    public boolean isLeft() {
        return (flags & 0x04) != 0;
    }

    public boolean isRight() {
        return (flags & 0x08) != 0;
    }

    public boolean isJump() {
        return (flags & 0x10) != 0;
    }

    public boolean isSneak() {
        return (flags & 0x20) != 0;
    }

    public boolean isSprint() {
        return (flags & 0x40) != 0;
    }

    public static class InputFlags {
        private int value = 0;

        public InputFlags forward() {
            value |= 0x01;
            return this;
        }

        public InputFlags backward() {
            value |= 0x02;
            return this;
        }

        public InputFlags left() {
            value |= 0x04;
            return this;
        }

        public InputFlags right() {
            value |= 0x08;
            return this;
        }

        public InputFlags jump() {
            value |= 0x10;
            return this;
        }

        public InputFlags sneak() {
            value |= 0x20;
            return this;
        }

        public InputFlags sprint() {
            value |= 0x40;
            return this;
        }

        public int build() {
            return value;
        }
    }
}