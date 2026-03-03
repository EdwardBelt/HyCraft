package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public abstract class NbtTag {
    private String name;

    public void write(PacketBuffer buffer) {
        buffer.writeByte(getId());

        if (name != null) {
            byte[] stringBytes = name.getBytes(StandardCharsets.UTF_8);
            buffer.writeShort(stringBytes.length);
            buffer.writeBytes(stringBytes);
        }

        writePayload(buffer);
    }

    abstract void writePayload(PacketBuffer buffer);
    abstract int getId();
}
