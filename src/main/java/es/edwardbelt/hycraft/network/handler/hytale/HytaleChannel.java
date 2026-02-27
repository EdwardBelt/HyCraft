package es.edwardbelt.hycraft.network.handler.hytale;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.ToServerPacket;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.netty.NettyUtil;
import com.hypixel.hytale.server.core.io.netty.PlayerChannelHandler;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import io.netty.channel.*;

import javax.annotation.Nullable;
import java.net.SocketAddress;

public class HytaleChannel extends AbstractChannel {
    private final Channel minecraftChannel;
    private final ClientConnection connection;
    private boolean closed = false;

    public HytaleChannel(Channel minecraftChannel, ClientConnection connection) {
        super(null);
        this.minecraftChannel = minecraftChannel;
        this.connection = connection;
    }

    @Override
    public ChannelConfig config() {
        return minecraftChannel.config();
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public boolean isActive() {
        return minecraftChannel.isActive() || !closed;
    }

    @Override
    public ChannelMetadata metadata() {
        return minecraftChannel.metadata();
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new FakeUnsafe();
    }

    @Override
    protected boolean isCompatible(EventLoop loop) {
        return true;
    }

    @Override
    protected SocketAddress localAddress0() {
        return minecraftChannel.localAddress();
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return minecraftChannel.remoteAddress();
    }

    @Override
    protected void doBind(SocketAddress localAddress) {
    }

    @Override
    protected void doDisconnect() {
        minecraftChannel.close();
    }

    @Override
    protected void doClose() {
        System.out.println("trying to close");
        if (closed) {
            System.out.println("already closed");
            return;
        }
        closed = true;

        System.out.println("closed");
        if (minecraftChannel.isOpen()) {
            System.out.println("closing mc as well");
            minecraftChannel.close();
        }
    }

    @Override
    protected void doBeginRead() {
    }

    @Nullable
    public PacketHandler getPacketHandler() {
        ChannelHandler channelHandler = this.pipeline().get(NettyUtil.HANDLER);
        if (channelHandler instanceof PlayerChannelHandler) {
            return ((PlayerChannelHandler) channelHandler).getHandler();
        }
        return null;
    }

    public void sendPacket(Packet packet) {
        PacketHandler handler = getPacketHandler();
        if (handler != null) {
            handler.handle((ToServerPacket) packet);
        }
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer in) {
        for (;;) {
            Object packet = in.current();
            if (packet == null) {
                break;
            }

            if (packet instanceof Packet[] packetArray) {
                for (Packet hytalePacket : packetArray) {
                    MinecraftServerBootstrap.get().getHytaleHandlerRegistry().handlePacket(hytalePacket, connection);
                }
            } else if (packet instanceof Packet hytalePacket) {
                MinecraftServerBootstrap.get().getHytaleHandlerRegistry().handlePacket(hytalePacket, connection);
            }

            in.remove();
        }
    }


    private class FakeUnsafe extends AbstractUnsafe {
        @Override
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            promise.setSuccess();
        }
    }
}