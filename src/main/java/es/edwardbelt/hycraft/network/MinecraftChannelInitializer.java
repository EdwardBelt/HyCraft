package es.edwardbelt.hycraft.network;

import es.edwardbelt.hycraft.network.handler.minecraft.MainPacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.codec.MinecraftFrameDecoder;
import es.edwardbelt.hycraft.protocol.codec.MinecraftFrameEncoder;
import es.edwardbelt.hycraft.protocol.codec.MinecraftPacketDecoder;
import es.edwardbelt.hycraft.protocol.codec.MinecraftPacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class MinecraftChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) {
        ClientConnection connection = MinecraftServerBootstrap.get().createConnection(channel);

        channel.pipeline()
                .addLast("mc-frame-decoder", new MinecraftFrameDecoder())
                .addLast("mc-packet-decoder", new MinecraftPacketDecoder(connection))
                .addLast("mc-frame-encoder", new MinecraftFrameEncoder())
                .addLast("mc-packet-encoder", new MinecraftPacketEncoder(connection))
                .addLast("mc-handler", new MainPacketHandler(connection));
    }
}