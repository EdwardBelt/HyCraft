package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class PlayerPositionPacket implements Packet {
    private final int teleportId;
    private final double x;
    private final double y;
    private final double z;
    private final double velocityX;
    private final double velocityY;
    private final double velocityZ;
    private final float yaw;
    private final float pitch;
    private final int flags;

    public PlayerPositionPacket(int teleportId, double x, double y, double z,
                                double velocityX, double velocityY, double velocityZ,
                                float yaw, float pitch, int flags) {
        this.teleportId = teleportId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(teleportId);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(velocityX);
        buffer.writeDouble(velocityY);
        buffer.writeDouble(velocityZ);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeInt(flags);
    }

    public static class TeleportFlags {
        private int value = 0;

        public TeleportFlags relativeX() {
            value |= 0x01;
            return this;
        }

        public TeleportFlags relativeY() {
            value |= 0x02;
            return this;
        }

        public TeleportFlags relativeZ() {
            value |= 0x04;
            return this;
        }

        public TeleportFlags relativeYaw() {
            value |= 0x08;
            return this;
        }

        public TeleportFlags relativePitch() {
            value |= 0x10;
            return this;
        }

        public TeleportFlags relativeVelocityX() {
            value |= 0x20;
            return this;
        }

        public TeleportFlags relativeVelocityY() {
            value |= 0x40;
            return this;
        }

        public TeleportFlags relativeVelocityZ() {
            value |= 0x80;
            return this;
        }

        public TeleportFlags rotateVelocity() {
            value |= 0x100;
            return this;
        }

        public int build() {
            return value;
        }
    }
}