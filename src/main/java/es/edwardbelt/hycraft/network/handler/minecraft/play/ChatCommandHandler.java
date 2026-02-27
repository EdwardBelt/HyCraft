package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.packets.interface_.ChatMessage;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ChatCommandPacket;

public class ChatCommandHandler implements PacketHandler<ChatCommandPacket> {
    @Override
    public void handle(ChatCommandPacket packet, ClientConnection connection) {
        ChatMessage messagePacket = new ChatMessage("/"+packet.getCommand());
        connection.getHytaleChannel().sendPacket(messagePacket);
    }
}
