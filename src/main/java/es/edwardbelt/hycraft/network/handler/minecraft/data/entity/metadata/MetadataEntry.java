package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MetadataEntry {
    private byte index;
    private MetadataValue value;

    public void serialize(PacketBuffer buffer) {
        buffer.writeUnsignedByte(index);
        buffer.writeVarInt(value.getTypeId());
        value.serialize(buffer);
    }
}
