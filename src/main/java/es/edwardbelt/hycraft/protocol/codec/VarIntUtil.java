package es.edwardbelt.hycraft.protocol.codec;

import io.netty.buffer.ByteBuf;

public final class VarIntUtil {
    private static final int VARINT_MAX_BYTES = 5;
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int readVarInt(ByteBuf buffer) {
        int value = 0;
        int position = 0;

        for (int i = 0; i < VARINT_MAX_BYTES; i++) {
            if (!buffer.isReadable()) {
                return -1;
            }

            byte currentByte = buffer.readByte();
            value |= (currentByte & SEGMENT_BITS) << (position * 7);

            if ((currentByte & CONTINUE_BIT) == 0) {
                return value;
            }

            position++;
        }

        throw new RuntimeException("VarInt is too big");
    }

    public static void writeVarInt(ByteBuf buffer, int value) {
        while ((value & ~SEGMENT_BITS) != 0) {
            buffer.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
        buffer.writeByte(value);
    }

    public static int getVarIntSize(int value) {
        for (int i = 1; i < VARINT_MAX_BYTES; i++) {
            if ((value & (-1 << (i * 7))) == 0) {
                return i;
            }
        }
        return VARINT_MAX_BYTES;
    }
}
