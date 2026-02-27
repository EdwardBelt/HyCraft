package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttributesPacket implements Packet {
    private int entityId;
    private List<Property> properties;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeVarInt(properties.size());
        for (Property property : properties) {
            property.write(buffer);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {
        private int attributeId;
        private double value;
        private List<Modifier> modifiers;

        public void write(PacketBuffer buffer) {
            buffer.writeVarInt(attributeId);
            buffer.writeDouble(value);
            buffer.writeVarInt(modifiers.size());
            for (Modifier modifier : modifiers) {
                modifier.write(buffer);
            }
        }

        public void read(PacketBuffer buffer) {
            this.attributeId = buffer.readVarInt();
            this.value = buffer.readDouble();
            int modifierCount = buffer.readVarInt();
            this.modifiers = new ArrayList<>(modifierCount);
            for (int i = 0; i < modifierCount; i++) {
                Modifier modifier = new Modifier();
                modifier.read(buffer);
                this.modifiers.add(modifier);
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modifier {
        private String id;
        private double amount;
        private byte operation;

        public void write(PacketBuffer buffer) {
            buffer.writeString(id);
            buffer.writeDouble(amount);
            buffer.writeByte(operation);
        }

        public void read(PacketBuffer buffer) {
            this.id = buffer.readString();
            this.amount = buffer.readDouble();
            this.operation = buffer.readByte();
        }
    }

    public static class Attributes {
        public static final int ARMOR = 0;
        public static final int ARMOR_TOUGHNESS = 1;
        public static final int ATTACK_DAMAGE = 2;
        public static final int ATTACK_KNOCKBACK = 3;
        public static final int ATTACK_SPEED = 4;
        public static final int BLOCK_BREAK_SPEED = 5;
        public static final int BLOCK_INTERACTION_RANGE = 6;
        public static final int BURNING_TIME = 7;
        public static final int CAMERA_DISTANCE = 8;
        public static final int ENTITY_INTERACTION_RANGE = 9;
        public static final int EXPLOSION_KNOCKBACK_RESISTANCE = 10;
        public static final int FALL_DAMAGE_MULTIPLIER = 11;
        public static final int FLYING_SPEED = 12;
        public static final int FOLLOW_RANGE = 13;
        public static final int GRAVITY = 14;
        public static final int JUMP_STRENGTH = 15;
        public static final int KNOCKBACK_RESISTANCE = 16;
        public static final int LUCK = 17;
        public static final int MAX_ABSORPTION = 18;
        public static final int MAX_HEALTH = 19;
        public static final int MINING_EFFICIENCY = 20;
        public static final int MOVEMENT_EFFICIENCY = 21;
        public static final int MOVEMENT_SPEED = 22;
        public static final int OXYGEN_BONUS = 23;
        public static final int SAFE_FALL_DISTANCE = 24;
        public static final int SCALE = 25;
        public static final int SPAWN_REINFORCEMENTS = 26;
        public static final int SNEAKING_SPEED = 27;
        public static final int STEP_HEIGHT = 28;
        public static final int SUBMERGED_MINING_SPEED = 29;
        public static final int SWEEPING_DAMAGE_RATIO = 30;
        public static final int TEMPT_RANGE = 31;
        public static final int WATER_MOVEMENT_EFFICIENCY = 32;
        public static final int WAYPOINT_RECEIVE_RANGE = 33;
        public static final int WAYPOINT_TRANSMIT_RANGE = 34;
    }

    public static class Operations {
        public static final byte ADD_VALUE = 0;
        public static final byte ADD_MULTIPLIED_BASE = 1;
        public static final byte ADD_MULTIPLIED_TOTAL = 2;
    }
}