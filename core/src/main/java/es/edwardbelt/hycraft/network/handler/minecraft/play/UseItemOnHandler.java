package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.use.UseManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.UseItemOnPacket;

public class UseItemOnHandler implements PacketHandler<UseItemOnPacket> {
    @Override
    public void handle(UseItemOnPacket packet, ClientConnection connection) {
        if (packet.getHand() == 1) return;
        PlayerRef playerRefWrapper = connection.getPlayerRef();
        Ref<EntityStore> entityRef = playerRefWrapper.getReference();
        Store<EntityStore> store = entityRef.getStore();
        EntityStore entityStore = store.getExternalData();
        World world = entityStore.getWorld();

        world.execute(() -> {
            boolean harvested = UseManager.get().harvestBlock(connection, packet.getPosition(), world, store, entityRef);
            if (harvested) return;
            UseManager.get().placeBlock(connection, packet.getPosition(), packet.getFace(), world, store, entityRef);
        });
    }
}
