package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CombatDeathPacket implements Packet {
    private int playerId;
    private String text;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(playerId);
        buffer.writeNBTStringTag(text);
    }
}
