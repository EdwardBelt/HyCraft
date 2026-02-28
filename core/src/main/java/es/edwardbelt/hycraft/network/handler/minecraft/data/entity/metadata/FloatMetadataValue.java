package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FloatMetadataValue implements MetadataValue {
    private float value;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeFloat(value);
    }

    @Override
    public int getTypeId() {
        return 3;
    }
}
