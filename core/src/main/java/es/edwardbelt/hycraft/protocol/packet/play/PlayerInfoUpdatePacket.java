package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.Property;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

import java.util.List;
import java.util.UUID;

public class PlayerInfoUpdatePacket implements Packet {
    private final byte actions;
    private final List<PlayerInfo> players;

    public PlayerInfoUpdatePacket(byte actions, List<PlayerInfo> players) {
        this.actions = actions;
        this.players = players;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeByte(actions);
        buffer.writeVarInt(players.size());

        for (PlayerInfo player : players) {
            buffer.writeUUID(player.uuid);

            if ((actions & 0x01) != 0) {
                buffer.writeString(player.name);
                buffer.writeVarInt(player.properties.size());
                for (Property property : player.properties) {
                    buffer.writeString(property.getName());
                    buffer.writeString(property.getValue());
                    buffer.writeBoolean(property.getSignature() != null);
                    if (property.getSignature() != null) {
                        buffer.writeString(property.getSignature());
                    }
                }
            }

            if ((actions & 0x02) != 0) {
                buffer.writeBoolean(player.chatSessionId != null);
                if (player.chatSessionId != null) {
                    buffer.writeUUID(player.chatSessionId);
                    buffer.writeLong(player.publicKeyExpiryTime);
                    buffer.writeVarInt(player.encodedPublicKey.length);
                    buffer.writeBytes(player.encodedPublicKey);
                    buffer.writeVarInt(player.publicKeySignature.length);
                    buffer.writeBytes(player.publicKeySignature);
                }
            }

            if ((actions & 0x04) != 0) {
                buffer.writeVarInt(player.gameMode);
            }

            if ((actions & 0x08) != 0) {
                buffer.writeBoolean(player.listed);
            }

            if ((actions & 0x10) != 0) {
                buffer.writeVarInt(player.ping);
            }

            if ((actions & 0x20) != 0) {
                buffer.writeBoolean(player.displayName != null);
                if (player.displayName != null) {
                    buffer.writeNBTStringTag(player.displayName);
                }
            }

            if ((actions & 0x40) != 0) {
                buffer.writeVarInt(player.priority);
            }

            if ((actions & 0x80) != 0) {
                buffer.writeBoolean(player.hatVisible);
            }
        }
    }

    public static class PlayerInfo {
        public UUID uuid;
        public String name;
        public List<Property> properties;
        public UUID chatSessionId;
        public long publicKeyExpiryTime;
        public byte[] encodedPublicKey;
        public byte[] publicKeySignature;
        public int gameMode;
        public boolean listed;
        public int ping;
        public String displayName;
        public int priority;
        public boolean hatVisible;

        public PlayerInfo(UUID uuid) {
            this.uuid = uuid;
        }
    }

    public static class Actions {
        private int value = 0;

        public Actions addPlayer() {
            value |= 0x01;
            return this;
        }

        public Actions initializeChat() {
            value |= 0x02;
            return this;
        }

        public Actions updateGameMode() {
            value |= 0x04;
            return this;
        }

        public Actions updateListed() {
            value |= 0x08;
            return this;
        }

        public Actions updateLatency() {
            value |= 0x10;
            return this;
        }

        public Actions updateDisplayName() {
            value |= 0x20;
            return this;
        }

        public Actions updateListPriority() {
            value |= 0x40;
            return this;
        }

        public Actions updateHat() {
            value |= 0x80;
            return this;
        }

        public byte build() {
            return (byte) value;
        }
    }
}