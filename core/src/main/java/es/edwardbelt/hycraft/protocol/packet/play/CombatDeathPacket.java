package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CombatDeathPacket implements Packet {
    private int playerId;
    private NbtString text;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(playerId);
        text.write(buffer);
    }
}
