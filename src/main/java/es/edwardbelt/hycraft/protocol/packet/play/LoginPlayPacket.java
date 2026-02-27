package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginPlayPacket implements Packet {
    private int entityId;
    private boolean isHardcore;
    private String[] dimensionNames;
    private int maxPlayers;
    private int viewDistance;
    private int simulationDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean doLimitedCrafting;
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
    private boolean enforcesSecureChat;

    public LoginPlayPacket(int entityId, boolean isHardcore, String[] dimensionNames,
                           int maxPlayers, int viewDistance, int simulationDistance,
                           boolean reducedDebugInfo, boolean enableRespawnScreen,
                           boolean doLimitedCrafting, int dimensionType, String dimensionName,
                           long hashedSeed, int gameMode, byte previousGameMode,
                           boolean isDebug, boolean isFlat, boolean hasDeathLocation,
                           String deathDimensionName, long deathLocation,
                           int portalCooldown, int seaLevel, boolean enforcesSecureChat) {
        this.entityId = entityId;
        this.isHardcore = isHardcore;
        this.dimensionNames = dimensionNames;
        this.maxPlayers = maxPlayers;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.enableRespawnScreen = enableRespawnScreen;
        this.doLimitedCrafting = doLimitedCrafting;
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
        this.enforcesSecureChat = enforcesSecureChat;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeBoolean(isHardcore);

        buffer.writeVarInt(dimensionNames.length);
        for (String dimension : dimensionNames) {
            buffer.writeString(dimension);
        }

        buffer.writeVarInt(maxPlayers);
        buffer.writeVarInt(viewDistance);
        buffer.writeVarInt(simulationDistance);
        buffer.writeBoolean(reducedDebugInfo);
        buffer.writeBoolean(enableRespawnScreen);
        buffer.writeBoolean(doLimitedCrafting);
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
        buffer.writeBoolean(enforcesSecureChat);
    }
}