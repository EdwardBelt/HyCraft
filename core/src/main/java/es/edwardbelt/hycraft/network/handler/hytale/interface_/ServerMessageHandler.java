package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.protocol.MaybeBool;
import com.hypixel.hytale.protocol.packets.interface_.ServerMessage;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtText;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.util.MessageUtil;

public class ServerMessageHandler implements PacketHandler<ServerMessage> {
    @Override
    public void handle(ServerMessage packet, ClientConnection connection) {
        if (packet.message == null) return;

        MessageUtil.send(connection, convertMessage(packet.message));
    }

    private NbtText convertMessage(FormattedMessage message) {
        NbtText nbt = new NbtText();

        String text = HytaleUtil.resolveMessageText(message);
        nbt.setText(text != null ? text.replace("\\n", "\n") : "");

        if (message.color != null) {
            nbt.setColor(message.color);
        }

        if (message.bold != MaybeBool.Null) {
            nbt.setBold(message.bold == MaybeBool.True);
        }

        if (message.italic != MaybeBool.Null) {
            nbt.setItalic(message.italic == MaybeBool.True);
        }

        if (message.underlined != MaybeBool.Null) {
            nbt.setUnderlined(message.underlined == MaybeBool.True);
        }

        if (message.children != null) {
            for (FormattedMessage child : message.children) {
                nbt.addChildren(convertMessage(child));
            }
        }

        return nbt;
    }
}
