package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.interface_.ServerMessage;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import com.hypixel.hytale.server.core.util.MessageUtil;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SystemMessagePacket;

import java.util.ArrayList;
import java.util.List;

public class ServerMessageHandler implements PacketHandler<ServerMessage> {
    @Override
    public void handle(ServerMessage packet, ClientConnection connection) {
        if (packet.message == null) return;

        List<String> messages = new ArrayList<>();
        buildMessages(messages, packet.message);

        messages.forEach(message -> {
            if (message.isEmpty()) return;
            SystemMessagePacket messagePacket = new SystemMessagePacket(message);
            connection.getChannel().writeAndFlush(messagePacket);
        });
    }

    private void buildMessages(List<String> list, FormattedMessage message) {
        list.add("");
        appendMessage(list, message);
    }

    private void appendMessage(List<String> list, FormattedMessage message) {
        String text = HytaleUtil.resolveMessageText(message);

        if (text != null) {
            String[] parts = text.split("\\\\n", -1);
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) {
                    list.add("");
                }

                int last = list.size() - 1;
                list.set(last, list.get(last) + parts[i]);
            }
        }

        if (message.children != null) {
            for (FormattedMessage child : message.children) {
                appendMessage(list, child);
            }
        }
    }
}
