package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntityEffectPacket implements Packet {
    private int entityId;
    private int effectId;
    private int amplifier;
    private int duration;
    private byte flags;

    public EntityEffectPacket(int entityId, int effectId, int amplifier, int duration, boolean isAmbient, boolean showParticles, boolean showIcon, boolean blend) {
        this.entityId = entityId;
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.duration = duration;
        this.flags = buildFlags(isAmbient, showParticles, showIcon, blend);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeVarInt(effectId);
        buffer.writeVarInt(amplifier);
        buffer.writeVarInt(duration);
        buffer.writeByte(flags);
    }

    public boolean isAmbient() {
        return (flags & 0x01) != 0;
    }

    public boolean showParticles() {
        return (flags & 0x02) != 0;
    }

    public boolean showIcon() {
        return (flags & 0x04) != 0;
    }

    public boolean shouldBlend() {
        return (flags & 0x08) != 0;
    }

    public static byte buildFlags(boolean isAmbient, boolean showParticles, boolean showIcon, boolean blend) {
        byte flags = 0;
        if (isAmbient) flags |= 0x01;
        if (showParticles) flags |= 0x02;
        if (showIcon) flags |= 0x04;
        if (blend) flags |= 0x08;
        return flags;
    }

    public int getDisplayLevel() {
        return amplifier + 1;
    }

    public boolean isInfinite() {
        return duration == -1;
    }

    public static class Effects {
        public static final int SPEED = 1;
        public static final int SLOWNESS = 2;
        public static final int HASTE = 3;
        public static final int MINING_FATIGUE = 4;
        public static final int STRENGTH = 5;
        public static final int INSTANT_HEALTH = 6;
        public static final int INSTANT_DAMAGE = 7;
        public static final int JUMP_BOOST = 8;
        public static final int NAUSEA = 9;
        public static final int REGENERATION = 10;
        public static final int RESISTANCE = 11;
        public static final int FIRE_RESISTANCE = 12;
        public static final int WATER_BREATHING = 13;
        public static final int INVISIBILITY = 14;
        public static final int BLINDNESS = 15;
        public static final int NIGHT_VISION = 16;
        public static final int HUNGER = 17;
        public static final int WEAKNESS = 18;
        public static final int POISON = 19;
        public static final int WITHER = 20;
        public static final int HEALTH_BOOST = 21;
        public static final int ABSORPTION = 22;
        public static final int SATURATION = 23;
        public static final int GLOWING = 24;
        public static final int LEVITATION = 25;
        public static final int LUCK = 26;
        public static final int UNLUCK = 27;
        public static final int SLOW_FALLING = 28;
        public static final int CONDUIT_POWER = 29;
        public static final int DOLPHINS_GRACE = 30;
        public static final int BAD_OMEN = 31;
        public static final int HERO_OF_THE_VILLAGE = 32;
        public static final int DARKNESS = 33;
    }
}