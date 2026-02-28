package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class SwingArmPacket implements Packet {
    private int hand;

    @Override
    public void read(PacketBuffer buffer) {
        this.hand = buffer.readVarInt();
    }

    public enum Hand {
        MAIN_HAND(0),
        OFF_HAND(1);

        private final int id;

        Hand(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Hand fromId(int id) {
            for (Hand hand : values()) {
                if (hand.id == id) {
                    return hand;
                }
            }
            return null;
        }
    }

    public Hand getHandEnum() {
        return Hand.fromId(hand);
    }

    public boolean isMainHand() {
        return hand == 0;
    }

    public boolean isOffHand() {
        return hand == 1;
    }
}