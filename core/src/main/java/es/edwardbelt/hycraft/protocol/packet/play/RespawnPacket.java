package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespawnPacket implements Packet {
    private int dimensionType;
    private String dimensionName;
    private long hashedSeed;
    private int gameMode;
    private byte previousGameMode;
    private boolean isDebug;
    private boolean isFlat;
    private boolean hasDeathLocation;
    private String deathDimensionName;
    private long deathLocation;
    private int portalCooldown;
    private int seaLevel;
    private byte dataKept;

    public RespawnPacket(int dimensionType, String dimensionName, long hashedSeed,
                         int gameMode, byte previousGameMode, boolean isDebug,
                         boolean isFlat, boolean hasDeathLocation, String deathDimensionName,
                         long deathLocation, int portalCooldown, int seaLevel, byte dataKept) {
        this.dimensionType = dimensionType;
        this.dimensionName = dimensionName;
        this.hashedSeed = hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
        this.hasDeathLocation = hasDeathLocation;
        this.deathDimensionName = deathDimensionName;
        this.deathLocation = deathLocation;
        this.portalCooldown = portalCooldown;
        this.seaLevel = seaLevel;
        this.dataKept = dataKept;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(dimensionType);
        buffer.writeString(dimensionName);
        buffer.writeLong(hashedSeed);
        buffer.writeUnsignedByte(gameMode);
        buffer.writeByte(previousGameMode);
        buffer.writeBoolean(isDebug);
        buffer.writeBoolean(isFlat);
        buffer.writeBoolean(hasDeathLocation);

        if (hasDeathLocation) {
            buffer.writeString(deathDimensionName);
            buffer.writeLong(deathLocation);
        }

        buffer.writeVarInt(portalCooldown);
        buffer.writeVarInt(seaLevel);
        buffer.writeByte(dataKept);
    }
}