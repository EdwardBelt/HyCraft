package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetCarriedSlotPacket;

public class SetCarriedSlotHandler implements PacketHandler<SetCarriedSlotPacket> {
    @Override
    public void handle(SetCarriedSlotPacket packet, ClientConnection connection) {
        PlayerRef playerRef = connection.getPlayerRef();
        if (playerRef == null) return;

        Ref<EntityStore> entityRef = playerRef.getReference();
        Store<EntityStore> store = entityRef.getStore();
        store.getExternalData().getWorld().execute(() -> {
            LivingEntity entity = store.getComponent(entityRef, Player.getComponentType());
            if (entity == null) return;

            entity.getInventory().setActiveHotbarSlot((byte) packet.getSlotId());
        });
    }
}
