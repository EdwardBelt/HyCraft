package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import lombok.Getter;

@Getter
public class NbtBoolean extends NbtByte {

    public NbtBoolean(boolean value, String name) {
        super(getByteByBool(value), name);
    }

    public NbtBoolean(boolean value) {
        this(value, null);
    }

    private static byte getByteByBool(boolean value) {
        return value ? (byte) 1 : 0;
    }
}
