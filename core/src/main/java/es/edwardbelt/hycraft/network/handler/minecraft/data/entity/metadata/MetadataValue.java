package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public interface MetadataValue {
    void serialize(PacketBuffer buffer);
    int getTypeId();
}
