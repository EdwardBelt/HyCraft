package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityMetadata {
    private final Map<Byte, MetadataEntry> metadatas;
    private final Set<Byte> dirtyIndexes;

    public EntityMetadata() {
        this.metadatas = new HashMap<>();
        this.dirtyIndexes = new HashSet<>();
    }

    public boolean isEmpty() {
        return metadatas.isEmpty();
    }

    public void set(int index, MetadataValue value) {
        MetadataEntry metadata = new MetadataEntry((byte) index, value);
        metadatas.put((byte) index, metadata);
        dirtyIndexes.add((byte) index);
    }

    public MetadataValue get(int index) {
        MetadataEntry entry = metadatas.get((byte) index);
        if (entry == null) return null;
        return entry.getValue();
    }

    public void serialize(PacketBuffer buffer) {
        dirtyIndexes.forEach((index) -> {
           MetadataEntry entry = metadatas.get(index);
           entry.serialize(buffer);
        });

        buffer.writeByte(255);
        dirtyIndexes.clear();
    }
}
