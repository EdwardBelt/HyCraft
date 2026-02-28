package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VarIntMetadataValue implements MetadataValue {
    private int value;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(value);
    }

    @Override
    public int getTypeId() {
        return 1;
    }
}
