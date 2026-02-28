package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class SetPlayerMovementFlagsPacket implements Packet {
    private byte flags;

    @Override
    public void read(PacketBuffer buffer) {
        this.flags = buffer.readByte();
    }

    public boolean isOnGround() {
        return (flags & 0x01) != 0;
    }

    public boolean isPushingAgainstWall() {
        return (flags & 0x02) != 0;
    }

    public static class PlayerFlags {
        private byte value = 0;

        public PlayerFlags onGround() {
            value |= 0x01;
            return this;
        }

        public PlayerFlags pushingAgainstWall() {
            value |= 0x02;
            return this;
        }

        public byte build() {
            return value;
        }
    }
}