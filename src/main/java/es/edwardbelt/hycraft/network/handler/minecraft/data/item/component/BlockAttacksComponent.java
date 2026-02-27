package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class BlockAttacksComponent implements Component {
    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeFloat(0);
        buffer.writeFloat(0);
        buffer.writeVarInt(0);
        buffer.writeFloat(0);
        buffer.writeFloat(0);
        buffer.writeFloat(0);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
        buffer.writeBoolean(false);
    }
}
