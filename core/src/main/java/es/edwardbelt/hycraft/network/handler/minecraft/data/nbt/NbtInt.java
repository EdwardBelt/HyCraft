package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class NbtInt extends NbtTag {
    private final int value;

    public NbtInt(int value, String name) {
        super(name);
        this.value = value;
    }

    public NbtInt(int value) {
        this(value, null);
    }

    @Override
    void writePayload(PacketBuffer buffer) {
        buffer.writeInt(value);
    }

    @Override
    int getId() {
        return 3;
    }
}
