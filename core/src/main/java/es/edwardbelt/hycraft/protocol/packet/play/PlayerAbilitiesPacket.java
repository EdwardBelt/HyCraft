package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerAbilitiesPacket implements Packet {
    private byte flags;
    private float flyingSpeed;
    private float fieldOfViewModifier;

    public PlayerAbilitiesPacket(byte flags, float flyingSpeed, float fieldOfViewModifier) {
        this.flags = flags;
        this.flyingSpeed = flyingSpeed;
        this.fieldOfViewModifier = fieldOfViewModifier;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeByte(flags);
        buffer.writeFloat(flyingSpeed);
        buffer.writeFloat(fieldOfViewModifier);
    }

    public static class PlayerAbilityFlags {
        public static final byte INVULNERABLE = 0x01;
        public static final byte FLYING = 0x02;
        public static final byte ALLOW_FLYING = 0x04;
        public static final byte CREATIVE_MODE = 0x08;
    }
}