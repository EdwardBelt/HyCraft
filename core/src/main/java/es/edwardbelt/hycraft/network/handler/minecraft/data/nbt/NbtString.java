package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class NbtString extends NbtTag {
    private final String value;

    public NbtString(String value, String name) {
        super(name);
        this.value = value;
    }

    public NbtString(String value) {
        this(value, null);
    }

    @Override
    void writePayload(PacketBuffer buffer) {
        byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
        buffer.writeShort(stringBytes.length);
        buffer.writeBytes(stringBytes);
    }

    @Override
    int getId() {
        return 8;
    }
}
