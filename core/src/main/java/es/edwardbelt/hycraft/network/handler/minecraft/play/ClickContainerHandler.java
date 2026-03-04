package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.gui.GuiManager;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory.InventoryManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ClickContainerPacket;

public class ClickContainerHandler implements PacketHandler<ClickContainerPacket> {
    @Override
    public void handle(ClickContainerPacket packet, ClientConnection connection) {
        PlayerRef playerRef = connection.getPlayerRef();
        if (playerRef == null) return;

        Ref<EntityStore> entityRef = playerRef.getReference();
        Store<EntityStore> store = entityRef.getStore();
        store.getExternalData().getWorld().execute(() -> {
            LivingEntity entity = store.getComponent(entityRef, Player.getComponentType());
            if (entity == null) return;

            Inventory inventory = entity.getInventory();

            if (packet.getWindowId() != 0) {
                GuiManager.get().clickGui(connection, inventory, packet.getSlot(), packet.getClickMode(), packet.getButton());
            } else {
                InventoryManager.get().handleClick(connection, inventory, packet.getSlot(), packet.getButton(), packet.getClickMode());
            }
        });
    }
}
