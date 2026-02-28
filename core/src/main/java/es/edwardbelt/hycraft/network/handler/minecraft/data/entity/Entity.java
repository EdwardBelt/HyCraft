package es.edwardbelt.hycraft.network.handler.minecraft.data.entity;

import es.edwardbelt.hycraft.api.entity.HyCraftEntity;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata.*;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.play.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Entity implements HyCraftEntity {
    private int id;
    private UUID uuid;
    private int type;

    private double x;
    private double y;
    private double z;

    private byte yaw;
    private byte pitch;
    private byte headYaw;

    private EntityMetadata metadata;
    private Map<SetEquipmentPacket.Type, ItemStack> equipment;

    private boolean onGround;

    private int data;

    private boolean spawned;

    public Entity(int id) {
        this.id = id;
        this.uuid = UUID.randomUUID();
        this.type = -1;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.yaw = 0;
        this.pitch = 0;
        this.headYaw = 0;
        this.data = 0;
        this.metadata = new EntityMetadata();
        this.equipment = new HashMap<>();
    }

    public void spawn(ClientConnection connection) {
        if (type == -1) type = 0;

        SpawnEntityPacket spawnPacket = new SpawnEntityPacket(this);
        connection.getChannel().writeAndFlush(spawnPacket);

        sendMetadata(connection);
        sendEquipment(connection);
        spawned = true;
    }

    public void sendMetadata(ClientConnection connection) {
        if (metadata == null || metadata.isEmpty()) return;
        SetEntityMetadataPacket metadataPacket = new SetEntityMetadataPacket(this);
        connection.getChannel().writeAndFlush(metadataPacket);
    }

    public void sendEquipment(ClientConnection connection) {
        if (equipment.isEmpty()) return;
        SetEquipmentPacket packet = new SetEquipmentPacket(id, equipment);
        connection.getChannel().writeAndFlush(packet);
    }

    public void despawn(ClientConnection connection) {
        RemoveEntitiesPacket packet = new RemoveEntitiesPacket(new int[]{getId()});
        connection.getChannel().writeAndFlush(packet);
        spawned = false;
    }

    public void setHealth(float health) {
        if (health < 0) health = 0;
        metadata.set(9, new FloatMetadataValue(health));
    }

    public float getHealth() {
        MetadataValue metadataValue = metadata.get(9);
        if (metadataValue == null) return 0;
        return ((FloatMetadataValue) metadataValue).getValue();
    }

    public void setEquipment(SetEquipmentPacket.Type type, ItemStack item) {
        equipment.put(type, item);
    }

    public void setItem(ItemStack item) {
        metadata.set(8, new SlotMetadataValue(item));
    }

    public void setPose(PoseMetadataValue.Pose pose) {
        metadata.set(6, new PoseMetadataValue(pose));
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotation(float yaw, float pitch) {
        this.pitch = (byte) (pitch * 256.0F / 360.0F);
        this.yaw = (byte) (yaw * 256.0F / 360.0F);
    }

    public void setHeadYaw(float yaw) {
        this.headYaw = (byte) (yaw * 256.0F / 360.0F);
    }

    public void teleport(ClientConnection connection, double x, double y, double z, float yaw, float pitch) {
        TeleportEntityPacket packet = new TeleportEntityPacket(id, x, y, z, yaw, pitch, onGround);
        connection.getChannel().writeAndFlush(packet);
        setPosition(x, y, z);
        setRotation(yaw, pitch);
    }

    public void move(ClientConnection connection, double toX, double toY, double toZ) {
        MoveEntityPacket packet = new MoveEntityPacket(id, toX-x, toY-y, toZ-z, onGround);
        connection.getChannel().writeAndFlush(packet);
        setPosition(toX, toY, toZ);
    }

    public void moveAndRot(ClientConnection connection, double toX, double toY, double toZ, float yaw, float pitch) {
        MoveAndRotEntityPacket packet = new MoveAndRotEntityPacket(id, toX-x, toY-y, toZ-z, yaw, pitch, onGround);
        connection.getChannel().writeAndFlush(packet);
        setPosition(toX, toY, toZ);
        setRotation(yaw, pitch);
    }

    public void rotate(ClientConnection connection, float yaw, float pitch) {
        RotateEntityPacket packet = new RotateEntityPacket(id, yaw, pitch, onGround);
        connection.getChannel().writeAndFlush(packet);
        setRotation(yaw, pitch);
    }

    public void rotateHead(ClientConnection connection, float headYaw) {
        RotateHeadEntityPacket headPacket = new RotateHeadEntityPacket(id, headYaw);
        connection.getChannel().writeAndFlush(headPacket);
        setHeadYaw(headYaw);
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(id);
        buffer.writeUUID(uuid);
        buffer.writeVarInt(type);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeByte(0);
        buffer.writeByte(pitch);
        buffer.writeByte(yaw);
        buffer.writeByte(headYaw);
        buffer.writeVarInt(data);
    }

    public void setCustomName(String name) {
        metadata.set(2, new OptionalTextMetadataValue(name));
        metadata.set(3, new BooleanMetadataValue(true));
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", headYaw=" + headYaw +
                ", data=" + data +
                '}';
    }
}
