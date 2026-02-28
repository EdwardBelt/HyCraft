package es.edwardbelt.hycraft.network.player;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.Vector3d;
import com.hypixel.hytale.protocol.io.netty.ProtocolUtil;
import com.hypixel.hytale.protocol.packets.connection.Ping;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import com.hypixel.hytale.protocol.packets.player.ClientReady;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import es.edwardbelt.hycraft.api.entity.HyCraftEntity;
import es.edwardbelt.hycraft.network.auth.CipherDecoder;
import es.edwardbelt.hycraft.network.auth.CipherEncoder;
import es.edwardbelt.hycraft.network.auth.EncryptionUtil;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleChannel;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.Chunk;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkCoordIntPair;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.GameProfile;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.blockbreak.BlockBreakManager;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.blockbreak.BlockBreakTracker;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory.InventoryCursor;
import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.packet.play.*;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class ClientConnection implements HyCraftConnection {
    private final Channel channel;
    private HytaleChannel hytaleChannel;
    private ConnectionState state;

    private byte[] pendingVerifyToken;
    private String pendingUsername;

    private GameProfile profile;
    private String username;
    private UUID uuid;
    private int protocolVersion;
    private int networkId;
    private ChunkBuffer chunkBuffer;
    private boolean initialized;
    private Ping pendingPing;
    private int viewDistance;
    private UUID nextWorldRespawn;

    private final MovementStates movementStates;
    public AtomicInteger clientChainId;

    private InventoryCursor cursor;

    private boolean cancelNextPlaceBlock;

    private long lastBreakTime = 0;
    private BlockBreakTracker blockBreakTracker;

    private String lastAttackInteraction;
    private Map<String, Long> itemIdsCooldowns;

    private float health;
    private Map<Integer, Entity> spawnedEntities;
    private Map<Byte, Position> tpConfirmations;

    private String lastNotificationItemId;
    private int lastNotificationQuantity;
    private long lastNotificationTime;

    private long lastAnimation;

    public ClientConnection(Channel channel) {
        this.channel = channel;
        this.state = ConnectionState.HANDSHAKING;
        this.chunkBuffer = new ChunkBuffer(this);
        this.clientChainId = new AtomicInteger(0);
        this.initialized = false;
        this.movementStates = new MovementStates();
        this.cursor = new InventoryCursor();
        this.spawnedEntities = new HashMap<>();
        this.tpConfirmations = new HashMap<>();
        this.itemIdsCooldowns = new HashMap<>();
    }

    public void enableEncryption(byte[] sharedSecret) throws Exception {
        Cipher decryptCipher = EncryptionUtil.createAESCipher(Cipher.DECRYPT_MODE, sharedSecret);
        Cipher encryptCipher = EncryptionUtil.createAESCipher(Cipher.ENCRYPT_MODE, sharedSecret);

        channel.pipeline().addBefore("mc-frame-decoder", "decrypt", new CipherDecoder(decryptCipher));
        channel.pipeline().addBefore("mc-frame-encoder", "encrypt", new CipherEncoder(encryptCipher));
    }

    public PlayerRef getPlayerRef() {
        return Universe.get().getPlayer(uuid);
    }

    public void addSpawnedEntity(int id, Entity entity) {
        spawnedEntities.put(id, entity);
    }

    public boolean isEntitySpawned(int id) {
        return spawnedEntities.containsKey(id);
    }

    public Entity getSpawnedEntity(int id) {
        return spawnedEntities.get(id);
    }

    public Map<Integer, HyCraftEntity> getEntities() {
        return Collections.unmodifiableMap(getSpawnedEntities());
    }

    public void removeEntities(int[] ids) {
        for (int id : ids) spawnedEntities.remove(id);
    }

    public void respawn(double x, double y, double z, float yaw, float pitch) {
        Direction direction = new Direction(yaw, pitch, 0);
        float[] minecraftYawPitch = HytaleUtil.getMinecraftYawPitch(direction);

        if (nextWorldRespawn != null) {
            RespawnPacket respawnPacket = new RespawnPacket(
                    0,
                    HytaleUtil.getDimensionName(nextWorldRespawn),
                    0,
                    0,
                    (byte) -1,
                    false,
                    false,
                    false,
                    null,
                    0,
                    0,
                    0,
                    (byte) (0x01 | 0x02)
            );

            channel.writeAndFlush(respawnPacket);
            nextWorldRespawn = null;
        }

        PlayerPositionPacket positionPacket = new PlayerPositionPacket(1, x, y, z, 0, 0, 0, minecraftYawPitch[0], minecraftYawPitch[1], 0);
        channel.writeAndFlush(positionPacket);

        GameEventPacket minecraftReadyChunksPacket = new GameEventPacket(13, 0);
        channel.writeAndFlush(minecraftReadyChunksPacket);

        CommandsPacket commandsPacket = new CommandsPacket(HytaleServer.get().getCommandManager().getCommandRegistration());
        channel.writeAndFlush(commandsPacket);

        ChunkCoordIntPair chunkCoords = Chunk.getChunkCoords(x, z);
        SetCenterChunkPacket centerChunkPacket = new SetCenterChunkPacket(chunkCoords);
        channel.writeAndFlush(centerChunkPacket);
        chunkBuffer.setLastChunkSent(chunkCoords);

        chunkBuffer.sendPendingChunks();

        ClientReady hytaleClientReadyPacket = new ClientReady(false, true);
        getHytaleChannel().sendPacket(hytaleClientReadyPacket);

        movementStates.idle = true;
        movementStates.horizontalIdle = true;
        movementStates.onGround = true;

        BlockBreakManager.get().changePlayerMiningSpeed(this);

        ClientMovement clientMovementPacket = new ClientMovement(movementStates, null, new Position(x, y, z), direction, direction, null, null, new Vector3d(0, 0, 0), 0, null);
        getHytaleChannel().sendPacket(clientMovementPacket);

        this.initialized = true;
    }

    public void checkLastCenterChunk(double x, double z) {
        ChunkCoordIntPair newChunk = Chunk.getChunkCoords(x, z);
        if (newChunk.equals(chunkBuffer.getLastChunkSent())) return;

        SetCenterChunkPacket centerChunkPacket = new SetCenterChunkPacket(newChunk);
        channel.writeAndFlush(centerChunkPacket);
        chunkBuffer.setLastChunkSent(newChunk);
    }

    public void disconnect(String reason) {
        cleanup();
        channel.close();
    }

    public void cleanup() {
        if (hytaleChannel != null && hytaleChannel.isOpen()) {
            ProtocolUtil.closeApplicationConnection(hytaleChannel);
        }
        hytaleChannel = null;
    }
}