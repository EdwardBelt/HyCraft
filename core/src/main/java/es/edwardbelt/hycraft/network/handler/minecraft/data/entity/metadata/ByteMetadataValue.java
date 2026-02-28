package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ByteMetadataValue implements MetadataValue {
    private byte value;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeByte(value);
    }

    @Override
    public int getTypeId() {
        return 0;
    }
}
