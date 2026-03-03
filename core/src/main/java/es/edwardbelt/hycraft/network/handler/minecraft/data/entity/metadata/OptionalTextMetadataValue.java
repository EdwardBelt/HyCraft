package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class OptionalTextMetadataValue implements MetadataValue {
    private final NbtString text;

    public OptionalTextMetadataValue(String value) {
        if (value == null) text = null;
        else this.text = new NbtString(value);
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeBoolean(text != null);
        if (text != null) text.write(buffer);
    }

    @Override
    public int getTypeId() {
        return 6;
    }
}
