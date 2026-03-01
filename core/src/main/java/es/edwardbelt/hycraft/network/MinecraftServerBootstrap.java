package es.edwardbelt.hycraft.network;

import com.hypixel.hytale.server.core.io.netty.NettyUtil;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.network.handler.minecraft.MinecraftHandlerRegistry;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleHandlerRegistry;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MinecraftServerBootstrap {
    private static MinecraftServerBootstrap INSTANCE;
    public static MinecraftServerBootstrap get() { return INSTANCE; }

    private Channel minecraftListener;
    private EventLoopGroup minecraftBossGroup;
    private EventLoopGroup minecraftWorkerGroup;

    private Map<Channel, ClientConnection> channelToConnection;
    private Map<UUID, ClientConnection> playerToConnection;

    @Getter
    private final MinecraftHandlerRegistry minecraftHandlerRegistry;
    @Getter
    private final HytaleHandlerRegistry hytaleHandlerRegistry;

    public MinecraftServerBootstrap() {
        INSTANCE = this;

        this.channelToConnection = new ConcurrentHashMap<>();
        this.playerToConnection = new ConcurrentHashMap<>();
        this.hytaleHandlerRegistry = new HytaleHandlerRegistry();

        this.minecraftHandlerRegistry = new MinecraftHandlerRegistry();
    }

    public void init() {
        try {
            System.out.println("Starting TCP listener for mc");

            minecraftBossGroup = NettyUtil.getEventLoopGroup(1, "MC-BossGroup");
            minecraftWorkerGroup = NettyUtil.getEventLoopGroup("MC-WorkerGroup");

            Class<? extends ServerChannel> serverChannelClass = NettyUtil.getServerChannel();

            ServerBootstrap mcBootstrap = new ServerBootstrap()
                    .group(minecraftBossGroup, minecraftWorkerGroup)
                    .channel(serverChannelClass)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new MinecraftChannelInitializer());

            int minecraftPort = HyCraft.get().getConfigManager().getMain().getPort();
            InetSocketAddress bindAddress = new InetSocketAddress(minecraftPort);

            ChannelFuture future = mcBootstrap.bind(bindAddress).sync();

            if (future.isSuccess()) {
                minecraftListener = future.channel();
                System.out.println("Minecraft TCP listener started on port " + minecraftPort);
            } else {
                System.out.println("Failed to bind Minecraft listener");
            }

        } catch (Exception e) {
            System.out.println("Failed to start Minecraft listener");
            System.out.println(e.getMessage());
        }
    }

    public void shutdown() {
        if (minecraftListener != null) {
            try {
                minecraftListener.close().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (minecraftBossGroup != null) {
            minecraftBossGroup.shutdownGracefully();
        }

        if (minecraftWorkerGroup != null) {
            minecraftWorkerGroup.shutdownGracefully();
        }
    }

    public ClientConnection createConnection(Channel channel) {
        ClientConnection connection = new ClientConnection(channel);
        channelToConnection.put(channel, connection);
        return connection;
    }

    public ClientConnection getConnection(UUID uuid) {
        return playerToConnection.get(uuid);
    }

    public Map<UUID, ClientConnection> getConnectionsByUUID() {
        return playerToConnection;
    }

    public void setConnection(UUID uuid, ClientConnection connection) {
        playerToConnection.put(uuid, connection);
    }

    public void disconnectConnection(ClientConnection connection) {
        disconnectConnection(connection, null);
    }

    public void disconnectConnection(ClientConnection connection, String reason) {
        connection.disconnect(reason);
        removeConnection(connection);
    }

    public void removeConnection(ClientConnection connection) {
        if (connection.getUuid() == null) return;
        playerToConnection.remove(connection.getUuid());
        channelToConnection.remove(connection.getChannel());
    }

}
