package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextMetadataValue implements MetadataValue {
    private String text;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeNBTStringTag(text);
    }

    @Override
    public int getTypeId() {
        return 5;
    }
}
