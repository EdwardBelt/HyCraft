package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class NbtCompound extends NbtTag {
    private final Map<String, NbtTag> tags;

    public NbtCompound(String key) {
        super(key);
        this.tags = new HashMap<>();
    }

    public NbtCompound() {
        this(null);
    }

    public NbtTag getTag(String key) {
        return tags.get(key);
    }

    public boolean hasTag(String key) {
        return tags.containsKey(key);
    }

    public void setString(String key, String value) {
        tags.put(key, new NbtString(value, key));
    }

    public String getString(String key) {
        NbtTag tag = tags.get(key);
        if (tag instanceof NbtString) {
            return ((NbtString) tag).getValue();
        }
        return null;
    }

    public void setInt(String key, int value) {
        tags.put(key, new NbtInt(value, key));
    }

    public int getInt(String key) {
        NbtTag tag = tags.get(key);
        if (tag instanceof NbtInt) {
            return ((NbtInt) tag).getValue();
        }
        return 0;
    }

    public NbtCompound createCompound(String key) {
        NbtCompound compound = new NbtCompound(key);
        tags.put(key, compound);
        return compound;
    }

    public NbtCompound getCompound(String key) {
        NbtTag tag = tags.get(key);
        if (tag instanceof NbtCompound) {
            return (NbtCompound) tag;
        }
        return null;
    }

    @Override
    void writePayload(PacketBuffer buffer) {
        tags.forEach((_, tag) -> tag.write(buffer));
        buffer.writeByte(0);
    }

    @Override
    int getId() {
        return 10;
    }
}
