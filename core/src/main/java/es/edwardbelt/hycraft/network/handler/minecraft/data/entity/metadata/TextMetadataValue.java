package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class TextMetadataValue implements MetadataValue {
    private final NbtString text;

    public TextMetadataValue(String value) {
        this.text = new NbtString(value);
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        text.write(buffer);
    }

    @Override
    public int getTypeId() {
        return 5;
    }
}
