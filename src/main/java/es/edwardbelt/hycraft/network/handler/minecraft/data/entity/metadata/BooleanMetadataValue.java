
package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BooleanMetadataValue implements MetadataValue {
    private boolean value;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeBoolean(value);
    }

    @Override
    public int getTypeId() {
        return 8;
    }
}
