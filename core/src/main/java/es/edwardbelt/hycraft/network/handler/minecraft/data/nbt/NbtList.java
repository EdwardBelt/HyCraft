package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class NbtList extends NbtTag {
    private final List<NbtTag> tags;
    private Integer type;

    public NbtList(String name, List<NbtTag> tags) {
        super(name);
        this.tags = tags;
    }

    public NbtList(String name) {
        this(name, new ArrayList<>());
    }

    public void addTag(NbtTag tag) {
        tags.add(tag);
        if (type == null) type = tag.getId();
    }

    @Override
    void writePayload(PacketBuffer buffer) {
        buffer.writeByte(type);
        buffer.writeInt(tags.size());
        tags.forEach(tag -> tag.writePayload(buffer));
    }

    @Override
    int getId() {
        return 9;
    }
}
