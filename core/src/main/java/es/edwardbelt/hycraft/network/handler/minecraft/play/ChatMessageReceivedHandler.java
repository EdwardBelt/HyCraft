package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.packets.interface_.ChatMessage;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ChatMessagePacket;

public class ChatMessageReceivedHandler implements PacketHandler<ChatMessagePacket> {
    @Override
    public void handle(ChatMessagePacket packet, ClientConnection connection) {
        ChatMessage messagePacket = new ChatMessage(packet.getMessage());
        connection.getHytaleChannel().sendPacket(messagePacket);
    }
}
