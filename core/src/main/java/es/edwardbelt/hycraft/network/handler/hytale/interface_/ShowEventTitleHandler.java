package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.protocol.packets.interface_.ShowEventTitle;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetSubtitlePacket;
import es.edwardbelt.hycraft.protocol.packet.play.SetTitleAnimationPacket;
import es.edwardbelt.hycraft.protocol.packet.play.SetTitlePacket;

public class ShowEventTitleHandler implements PacketHandler<ShowEventTitle> {
    @Override
    public void handle(ShowEventTitle packet, ClientConnection connection) {
        int fadeIn = (int) (packet.fadeInDuration * 20);
        int fadeOut = (int) (packet.fadeOutDuration * 20);
        int duration = (int) (packet.duration * 20);
        FormattedMessage titleMessage = packet.primaryTitle;
        FormattedMessage subtitleMessage = packet.secondaryTitle;

        SetTitleAnimationPacket animationPacket = new SetTitleAnimationPacket(fadeIn, duration, fadeOut);
        connection.getChannel().write(animationPacket);

        String title = null;
        String subtitle = null;

        if (titleMessage != null) {
            title = HytaleUtil.resolveMessageText(titleMessage);
            if (subtitleMessage != null) subtitle = HytaleUtil.resolveMessageText(subtitleMessage);
        } else if (subtitleMessage != null) {
            title = HytaleUtil.resolveMessageText(subtitleMessage);
        }

        if (title != null) {
            SetTitlePacket titlePacket = new SetTitlePacket(title);
            connection.getChannel().write(titlePacket);
        }

        if (subtitle != null) {
            SetSubtitlePacket subtitlePacket = new SetSubtitlePacket(subtitle);
            connection.getChannel().write(subtitlePacket);
        }

        connection.getChannel().flush();
    }
}
