package es.edwardbelt.hycraft.protocol.io;

import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import es.edwardbelt.hycraft.protocol.codec.VarIntUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Getter;
import net.kyori.adventure.nbt.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Getter
public class PacketBuffer {
    private final ByteBuf buffer;

    public PacketBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public int readVarInt() {
        return VarIntUtil.readVarInt(buffer);
    }

    public void writeVarInt(int value) {
        VarIntUtil.writeVarInt(buffer, value);
    }

    public long readVarLong() {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buffer.readByte();
            value |= (long) (currentByte & 0x7F) << position;

            if ((currentByte & 0x80) == 0) break;

            position += 7;

            if (position >= 64) {
                throw new RuntimeException("VarLong is too big");
            }
        }

        return value;
    }

    public void writeVarLong(long value) {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                buffer.writeByte((int) value);
                return;
            }

            buffer.writeByte((int) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    public void writeBytes(byte[] bytes) {
        buffer.writeBytes(bytes);
    }

    public String readString() {
        return readString(ProtocolConstants.MAX_STRING_LENGTH);
    }

    public int readUnsignedByte() {
        return buffer.readUnsignedByte();
    }

    public void writeUnsignedByte(int value) {
        buffer.writeByte(value);
    }

    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    public void writeUnsignedShort(int value) {
        buffer.writeShort(value);
    }

    public void writeShortLE(int value) {
        buffer.writeShortLE(value);
    }

    public void readBytes(byte[] arr) {
        buffer.readBytes(arr);
    }

    public short readShortLE() {
        return buffer.readShortLE();
    }

    public void writeLongLE(long value) {
        buffer.writeLongLE(value);
    }

    public long readLongLE() {
        return buffer.readLongLE();
    }

    public void writeLpVec3(double x, double y, double z) {
        double ax = clamp(x);
        double ay = clamp(y);
        double az = clamp(z);

        double magnitude = Math.max(Math.abs(ax), Math.max(Math.abs(ay), Math.abs(az)));

        if (magnitude < MIN_MAGNITUDE) {
            writeByte(0);
            return;
        }

        long mag = (long) Math.ceil(magnitude);
        boolean extended = (mag & 3L) != mag;
        long magBits = extended ? (mag & 3L) | 4L : mag;

        long xBits = encode(ax / (double)mag) << 3;
        long yBits = encode(ay / (double)mag) << 18;
        long zBits = encode(az / (double)mag) << 33;

        long packed = magBits | xBits | yBits | zBits;

        writeByte((byte)(int) packed);
        writeByte((byte)(int)(packed >> 8));
        writeInt((int)(packed >> 16));

        if (extended) {
            writeVarInt((int)(mag >> 2));
        }
    }

    public void writeNBTStringTag(String str) {
        byte[] stringBytes = str.getBytes(StandardCharsets.UTF_8);

        buffer.writeByte(8);
        buffer.writeShort(stringBytes.length);
        buffer.writeBytes(stringBytes);
    }

    public String readString(int maxLength) {
        int length = readVarInt();

        if (length > maxLength * 4) {
            throw new RuntimeException("String length exceeds maximum: " + length);
        }

        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        String str = new String(bytes, StandardCharsets.UTF_8);

        if (str.length() > maxLength) {
            throw new RuntimeException("String is too long: " + str.length());
        }

        return str;
    }

    public void writeString(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        if (bytes.length > ProtocolConstants.MAX_STRING_LENGTH * 4) {
            throw new RuntimeException("String is too long");
        }

        writeVarInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public void writeBoolean(boolean value) {
        buffer.writeBoolean(value);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void writeByte(int value) {
        buffer.writeByte(value);
    }

    public short readShort() {
        return buffer.readShort();
    }

    public void writeShort(int value) {
        buffer.writeShort(value);
    }

    public int readInt() {
        return buffer.readInt();
    }

    public int readIntLE() {
        return buffer.readIntLE();
    }

    public void writeIntLE(int value) {
        buffer.writeIntLE(value);
    }

    public void writeInt(int value) {
        buffer.writeInt(value);
    }

    public long readLong() {
        return buffer.readLong();
    }

    public void writeLong(long value) {
        buffer.writeLong(value);
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    public UUID readUUID() {
        long mostSigBits = buffer.readLong();
        long leastSigBits = buffer.readLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public void writeUUID(UUID uuid) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public int readableBytes() {
        return buffer.readableBytes();
    }

    public boolean isReadable() {
        return buffer.isReadable();
    }

    public void markReaderIndex() {
        buffer.markReaderIndex();
    }

    public void resetReaderIndex() {
        buffer.resetReaderIndex();
    }

    private static final double MAX_VALUE = 1.7179869183E10D;
    private static final double MIN_MAGNITUDE = 3.051944088384301E-5D;
    private static final double SCALE = 32766.0D;

    private static double clamp(double v) {
        return Double.isNaN(v) ? 0.0 : Math.max(-MAX_VALUE, Math.min(MAX_VALUE, v));
    }

    private static long encode(double v) {
        return Math.round((v * 0.5 + 0.5) * SCALE);
    }
}
