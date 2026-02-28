package es.edwardbelt.hycraft.protocol.packet;

import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.PacketDirection;
import es.edwardbelt.hycraft.protocol.packet.configuration.*;
import es.edwardbelt.hycraft.protocol.packet.handshake.HandshakePacket;
import es.edwardbelt.hycraft.protocol.packet.login.*;
import es.edwardbelt.hycraft.protocol.packet.play.*;
import es.edwardbelt.hycraft.protocol.packet.status.PingRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.PingResponsePacket;
import es.edwardbelt.hycraft.protocol.packet.status.StatusRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.StatusResponsePacket;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {
    private static final Map<ConnectionState, Map<PacketDirection, Map<Integer, PacketInfo>>> PACKETS = new HashMap<>();
    private static final Map<Class<? extends Packet>, Integer> PACKETS_BY_TYPE = new HashMap<>();

    static {
        // handshake
        register(0, ConnectionState.HANDSHAKING, PacketDirection.SERVERBOUND, "Handshake", HandshakePacket.class);

        // status
        register(0, ConnectionState.STATUS, PacketDirection.SERVERBOUND, "StatusRequest", StatusRequestPacket.class);
        register(0, ConnectionState.STATUS, PacketDirection.CLIENTBOUND, "StatusResponse", StatusResponsePacket.class);
        register(1, ConnectionState.STATUS, PacketDirection.SERVERBOUND, "PingRequest", PingRequestPacket.class);
        register(1, ConnectionState.STATUS, PacketDirection.CLIENTBOUND, "PingResponse", PingResponsePacket.class);

        // login
        register(0, ConnectionState.LOGIN, PacketDirection.SERVERBOUND, "LoginStart", LoginStartPacket.class);
        register(1, ConnectionState.LOGIN, PacketDirection.CLIENTBOUND, "EncryptionRequest", EncryptionRequestPacket.class);
        register(1, ConnectionState.LOGIN, PacketDirection.SERVERBOUND, "EncryptionResponse", EncryptionResponsePacket.class);
        register(2, ConnectionState.LOGIN, PacketDirection.CLIENTBOUND, "LoginSuccess", LoginSuccessPacket.class);
        register(3, ConnectionState.LOGIN, PacketDirection.SERVERBOUND, "LoginAcknowledged", LoginAcknowledgedPacket.class);

        // config
        register(0, ConnectionState.CONFIGURATION, PacketDirection.SERVERBOUND, "ClientInformation", ClientInformationPacket.class);
        register(2, ConnectionState.CONFIGURATION, PacketDirection.SERVERBOUND, "PluginMessage", PluginMessagePacket.class);
        register(3, ConnectionState.CONFIGURATION, PacketDirection.CLIENTBOUND, "FinishConfiguration", FinishConfigurationPacket.class);
        register(3, ConnectionState.CONFIGURATION, PacketDirection.SERVERBOUND, "FinishConfiguration", AckFinishConfigurationPacket.class);
        register(7, ConnectionState.CONFIGURATION, PacketDirection.CLIENTBOUND, "RegistryData", RegistryDataPacket.class);
        register(7, ConnectionState.CONFIGURATION, PacketDirection.SERVERBOUND, "SelectKnownPacks", ResponseKnownPacksPacket.class);
        register(14, ConnectionState.CONFIGURATION, PacketDirection.CLIENTBOUND, "SelectKnownPacks", SendKnownPacksPacket.class);

        // play
        register(0, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "AcceptTeleportation", ConfirmTeleportPacket.class);
        register(1, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SpawnEntity", SpawnEntityPacket.class);
        register(2, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "Animate", EntityAnimationPacket.class);
        register(4, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "AcknowledgeBlockChange", AcknowledgeBlockChangePacket.class);
        register(5, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetBlockDestroyStage", SetBlockDestroyStagePacket.class);
        register(6, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "ChatCommand", ChatCommandPacket.class);
        register(8, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "Chat", ChatMessagePacket.class);
        register(8, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "BlockUpdate", BlockUpdatePacket.class);
        register(11, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "ClientCommand", ClientCommandPacket.class);
        register(12, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "TickEnd", ClientTickEndPacket.class);
        register(16, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "Commands", CommandsPacket.class);
        register(17, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "ContainerClick", ClickContainerPacket.class);
        register(18, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "ContainerSetContent", SetContainerContentPacket.class);
        register(22, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetCooldown", SetCooldownPacket.class);
        register(25, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "Interact", EntityInteractPacket.class);
        register(27, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "KeepAlive", KeepAliveResponsePacket.class);
        register(29, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SetPlayerPosition", SetPlayerPositionPacket.class);
        register(30, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SetPlayerPositionAndRotation", SetPlayerPositionAndRotationPacket.class);
        register(31, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SetPlayerRotation", SetPlayerRotationPacket.class);
        register(32, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SetPlayerMovementFlags", SetPlayerMovementFlagsPacket.class);
        register(34, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "EntityEvent", EntityEventPacket.class);
        register(35, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "EntityPositionSync", TeleportEntityPacket.class);
        register(37, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "UnloadChunk", UnloadChunkPacket.class);
        register(38, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "GameEvent", GameEventPacket.class);
        register(40, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "PlayerAction", PlayerActionPacket.class);
        register(41, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "PlayerCommand", PlayerCommandPacket.class);
        register(41, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "HurtAnimation", HurtAnimationPacket.class);
        register(42, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "PlayerInput", PlayerInputPacket.class);
        register(43, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "KeepAlive", KeepAlivePacket.class);
        register(44, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "LevelChunkWithLight", LevelChunkWithLightPacket.class);
        register(45, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "WorldEvent", WorldEventPacket.class);
        register(48, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "LoginPlay", LoginPlayPacket.class);
        register(51, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "MoveEntityPos", MoveEntityPacket.class);
        register(52, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "MoveEntityPosRot", MoveAndRotEntityPacket.class);
        register(52, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SetCarriedSlot", SetCarriedSlotPacket.class);
        register(54, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "MoveEntityRot", RotateEntityPacket.class);
        register(60, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "SwingArm", SwingArmPacket.class);
        register(62, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "PlayerAbilities", PlayerAbilitiesPacket.class);
        register(63, ConnectionState.PLAY, PacketDirection.SERVERBOUND, "UseItemOn", UseItemOnPacket.class);
        register(66, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "PlayerCombatKill", CombatDeathPacket.class);
        register(67, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "PlayerInfoRemove", PlayerInfoRemovePacket.class);
        register(68, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "PlayerInfoUpdate", PlayerInfoUpdatePacket.class);
        register(70, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "PlayerPosition", PlayerPositionPacket.class);
        register(75, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "RemoveEntities", RemoveEntitiesPacket.class);
        register(80, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "Respawn", RespawnPacket.class);
        register(81, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "RotateHead", RotateHeadEntityPacket.class);
        register(82, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SectionBlocksUpdate", UpdateSectionBlocksPacket.class);
        register(84, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "ServerData", ServerDataPacket.class);
        register(85, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetActionBarText", SetActionBarPacket.class);
        register(92, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetCenterChunk", SetCenterChunkPacket.class);
        register(97, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetEntityMetadata", SetEntityMetadataPacket.class);
        register(99, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetEntityVelocity", SetEntityVelocityPacket.class);
        register(100, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetEquipment", SetEquipmentPacket.class);
        register(102, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetHealth", SetHealthPacket.class);
        register(103, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetHeldSlot", SetHeldSlotPacket.class);
        register(110, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetSubtitleText", SetSubtitlePacket.class);
        register(111, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "UpdateTime", UpdateTimePacket.class);
        register(112, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetTitleText", SetTitlePacket.class);
        register(113, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SetTitlesAnimation", SetTitleAnimationPacket.class);
        register(114, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "EntitySound", EntitySoundPacket.class);
        register(115, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "Sound", SoundPacket.class);
        register(119, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "SystemChat", SystemMessagePacket.class);
        register(129, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "UpdateAttributes", UpdateAttributesPacket.class);
        register(130, ConnectionState.PLAY, PacketDirection.CLIENTBOUND, "EntityEffect", EntityEffectPacket.class);
    }

    private static void register(int id, ConnectionState connectionState, PacketDirection packetDirection, String name, Class<? extends Packet> type) {
        PACKETS.putIfAbsent(connectionState, new HashMap<>());
        PACKETS.get(connectionState).putIfAbsent(packetDirection, new HashMap<>());
        PacketInfo existing = PACKETS.get(connectionState).get(packetDirection).get(id);
        if (existing != null) {
            throw new IllegalStateException("Duplicate packet ID " + id + ": '" + name + "' '" + connectionState + ":" + packetDirection + "' conflicts with '" + existing.name() + "'");
        } else {
            PacketInfo info = new PacketInfo(id, connectionState, packetDirection, name, type);
            PACKETS.get(connectionState).get(packetDirection).put(id, info);
            PACKETS_BY_TYPE.put(info.packet, info.id);
        }
    }

    public static Integer getPacketByType(Class<? extends Packet> clazz) {
        return PACKETS_BY_TYPE.get(clazz);
    }

    public static Packet createPacket(int id, ConnectionState connectionState, PacketDirection packetDirection) {
        PACKETS.putIfAbsent(connectionState, new HashMap<>());
        PACKETS.get(connectionState).putIfAbsent(packetDirection, new HashMap<>());

        PacketInfo info = PACKETS.get(connectionState).get(packetDirection).get(id);

        if (info == null) {
            System.out.println("Found no packet with ID '" + id + "' and connection state '" + connectionState + "' and packet direction '" + packetDirection + "'");
            return null;
        }

        Class<? extends Packet> clazz = info.packet;
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("error while creating packet instance");
        }

        return null;
    }

    public static record PacketInfo(int id, ConnectionState connectionState, PacketDirection packetDirection, String name, Class<? extends Packet> packet) {
    }
}
