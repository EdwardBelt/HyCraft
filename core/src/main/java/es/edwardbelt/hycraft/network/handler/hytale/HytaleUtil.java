package es.edwardbelt.hycraft.network.handler.hytale;

import com.hypixel.hytale.protocol.CachedPacket;
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.HytaleServerConfig;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.ProtocolVersion;
import com.hypixel.hytale.server.core.io.handlers.SetupPacketHandler;
import com.hypixel.hytale.server.core.io.netty.NettyUtil;
import com.hypixel.hytale.server.core.io.netty.PlayerChannelHandler;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.MessageUtil;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import es.edwardbelt.hycraft.util.reflection.MethodAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BooleanSupplier;

public class HytaleUtil {
    private static final MethodAccessor HANDLER_INIT_STAGE_METHOD = Reflections.getMethod(PacketHandler.class, "initStage", String.class, Duration.class, BooleanSupplier.class);
    private static final Map<Class<? extends Packet>, MethodAccessor> PACKET_DESERIALIZE_METHODS = new HashMap<>();

    public static Packet transformPacket(Packet packet) {
        if (packet instanceof CachedPacket<?> cachedPacket) {
            Class<? extends Packet> packetType = cachedPacket.getPacketType();
            ByteBuf buf = Unpooled.buffer();
            cachedPacket.serialize(buf);

            MethodAccessor method = PACKET_DESERIALIZE_METHODS.get(packetType);
            if (method == null) {
                method = Reflections.getMethod(packetType, "deserialize", ByteBuf.class, int.class);
                PACKET_DESERIALIZE_METHODS.put(packetType, method);
            }

            packet = (Packet) method.invoke(null, buf, 0);
        }

        return packet;
    }

    public static Class<? extends Packet> getPacketClazz(Packet packet) {
        if (packet instanceof CachedPacket<?> cachedPacket) {
            return cachedPacket.getPacketType();
        }

        return packet.getClass();
    }

    public static void createPlayer(ClientConnection connection) {
        UUID uuid = connection.getUuid();
        String username = connection.getUsername();

        try {
            HytaleChannel hytaleChannel = new HytaleChannel(connection.getChannel(), connection);
            ChannelFuture future = connection.getChannel().eventLoop().register(hytaleChannel);
            future.syncUninterruptibly();

            connection.setHytaleChannel(hytaleChannel);

            PlayerAuthentication auth = new PlayerAuthentication(uuid, username);

            ProtocolVersion hytaleProtocol = new ProtocolVersion(ProtocolConstants.HYTALE_PROTOCOL_CRC);

            SetupPacketHandler setupHandler = new SetupPacketHandler(
                    hytaleChannel,
                    hytaleProtocol,
                    "en-US",
                    auth
            );

            hytaleChannel.pipeline().addLast(NettyUtil.HANDLER, new PlayerChannelHandler(setupHandler));

            HytaleServerConfig.TimeoutProfile timeouts = HytaleServer.get().getConfig().getConnectionTimeouts();
            HANDLER_INIT_STAGE_METHOD.invoke(setupHandler, "initial", timeouts.getInitial(), (BooleanSupplier) () -> true);
            setupHandler.registered(null);
        } catch (Exception e) {
            System.err.println("Failed to create Hytale player for: " + username);
            e.printStackTrace();
            MinecraftServerBootstrap.get().disconnectConnection(connection, "Failed to create Hytale player!");
        }
    }

    public static String getDimensionName(UUID uuid) {
        return Universe.get().getWorld(uuid).getName();
    }

    public static String resolveMessageText(FormattedMessage message) {
        if (message.rawText != null) {
            return message.rawText;
        } else if (message.messageId != null) {
            String translation = I18nModule.get().getMessage("en-US", message.messageId);
            if (translation == null) translation = "";

            return MessageUtil.formatText(
                    translation,
                    message.params,
                    message.messageParams
            );
        }
        return null;
    }

    public static Direction getHytaleDirection(float mcYaw, float mcPitch) {
        float hytaleYawDegrees = 180.0f - mcYaw;

        while (hytaleYawDegrees > 180.0f) hytaleYawDegrees -= 360.0f;
        while (hytaleYawDegrees <= -180.0f) hytaleYawDegrees += 360.0f;

        float hytalePitchDegrees = -mcPitch;

        float hytaleYaw = (float) Math.toRadians(hytaleYawDegrees);
        float hytalePitch = (float) Math.toRadians(hytalePitchDegrees);

        return new Direction(hytaleYaw, hytalePitch, 0.0f);
    }

    public static float[] getMinecraftYawPitch(Direction direction) {
        return getMinecraftYawPitch(direction.yaw, direction.pitch);
    }

    public static float[] getMinecraftYawPitch(float yaw, float pitch) {
        float mcYaw = getMinecraftYaw(yaw);
        float mcPitch = getMinecraftPitch(pitch);
        return new float[]{mcYaw, mcPitch};
    }

    public static float getMinecraftYaw(float yaw) {
        float hytaleYawDegrees = (float) Math.toDegrees(yaw);
        float mcYaw = 180.0f - hytaleYawDegrees;
        while (mcYaw > 180.0f) mcYaw -= 360.0f;
        while (mcYaw <= -180.0f) mcYaw += 360.0f;
        return mcYaw;
    }

    public static float getMinecraftPitch(float pitch) {
        float hytalePitchDegrees = (float) Math.toDegrees(pitch);
        return -hytalePitchDegrees;
    }
}
