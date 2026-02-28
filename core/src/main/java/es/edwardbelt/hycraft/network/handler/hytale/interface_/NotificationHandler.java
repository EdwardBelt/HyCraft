package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.packets.interface_.Notification;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetActionBarPacket;
import es.edwardbelt.hycraft.util.ItemUtil;
import es.edwardbelt.hycraft.util.LanguageUtil;

public class NotificationHandler implements PacketHandler<Notification> {
    @Override
    public void handle(Notification packet, ClientConnection connection) {
        if (packet.item != null) {
            Item itemConfig = ItemUtil.getItemConfig(packet.item.itemId);
            String itemId = packet.item.itemId;
            int quantity = packet.item.quantity;
            String name = LanguageUtil.getMessage(itemConfig.getTranslationKey());

            long currentTime = System.currentTimeMillis();
            boolean isSameItem = itemId.equals(connection.getLastNotificationItemId());
            boolean isWithinTimeWindow = (currentTime - connection.getLastNotificationTime()) < 2000;

            if (isSameItem && isWithinTimeWindow) {
                connection.setLastNotificationQuantity(connection.getLastNotificationQuantity() + quantity);
            } else {
                connection.setLastNotificationItemId(itemId);
                connection.setLastNotificationQuantity(quantity);
            }

            connection.setLastNotificationTime(currentTime);

            String message = "§a+" + connection.getLastNotificationQuantity() + " §b" + name;
            SetActionBarPacket actionBarPacket = new SetActionBarPacket(message);
            connection.getChannel().writeAndFlush(actionBarPacket);
        }
    }
}
