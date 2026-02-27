package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class ClientInformationPacket implements Packet {
    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private int displayedSkinParts;
    private MainHand mainHand;
    private boolean enableTextFiltering;
    private boolean allowServerListings;
    private ParticleStatus particleStatus;

    @Override
    public void read(PacketBuffer buffer) {
        this.locale = buffer.readString(16);
        this.viewDistance = buffer.readByte();
        this.chatMode = ChatMode.fromId(buffer.readVarInt());
        this.chatColors = buffer.readBoolean();
        this.displayedSkinParts = buffer.readUnsignedByte();
        this.mainHand = MainHand.fromId(buffer.readVarInt());
        this.enableTextFiltering = buffer.readBoolean();
        this.allowServerListings = buffer.readBoolean();
        this.particleStatus = ParticleStatus.fromId(buffer.readVarInt());
    }

    public enum ChatMode {
        ENABLED(0),
        COMMANDS_ONLY(1),
        HIDDEN(2);

        private final int id;

        ChatMode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ChatMode fromId(int id) {
            for (ChatMode mode : values()) {
                if (mode.id == id) {
                    return mode;
                }
            }
            return ENABLED;
        }
    }

    public enum MainHand {
        LEFT(0),
        RIGHT(1);

        private final int id;

        MainHand(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static MainHand fromId(int id) {
            return id == 0 ? LEFT : RIGHT;
        }
    }

    public enum ParticleStatus {
        ALL(0),
        DECREASED(1),
        MINIMAL(2);

        private final int id;

        ParticleStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ParticleStatus fromId(int id) {
            for (ParticleStatus status : values()) {
                if (status.id == id) {
                    return status;
                }
            }
            return ALL;
        }
    }
}