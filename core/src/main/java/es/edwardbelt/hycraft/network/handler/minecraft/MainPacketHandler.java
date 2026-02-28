package es.edwardbelt.hycraft.network.handler.minecraft;

import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MainPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private final ClientConnection connection;

    public MainPacketHandler(ClientConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        try {
            MinecraftServerBootstrap.get().getMinecraftHandlerRegistry().handlePacket(packet, connection);
        } catch (Exception e) {
            System.err.println("Error handling packet in state " + connection.getState());
            e.printStackTrace();
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("MC client disconnected: " + connection.getUsername());
        MinecraftServerBootstrap.get().removeConnection(connection);
        connection.cleanup();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (!(cause instanceof java.nio.channels.ClosedChannelException)) {
            System.err.println("MC handler exception caught");
            cause.printStackTrace();
        }
        MinecraftServerBootstrap.get().removeConnection(connection);
        connection.cleanup();
        ctx.close();
    }
}
