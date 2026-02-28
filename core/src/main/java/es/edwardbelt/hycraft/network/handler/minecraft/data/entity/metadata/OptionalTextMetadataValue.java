package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OptionalTextMetadataValue implements MetadataValue {
    private String text;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeBoolean(text != null);
        if (text != null) buffer.writeNBTStringTag(text);
    }

    @Override
    public int getTypeId() {
        return 6;
    }
}
