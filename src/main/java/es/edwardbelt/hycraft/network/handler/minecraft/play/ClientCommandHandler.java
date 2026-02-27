package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ClientCommandPacket;

public class ClientCommandHandler implements PacketHandler<ClientCommandPacket> {
    @Override
    public void handle(ClientCommandPacket packet, ClientConnection connection) {
        /*if (packet.getAction() != 0) return;
        PlayerRef playerRef = connection.getPlayerRef();
        Ref<EntityStore> entityRef = playerRef.getReference();
        Store<EntityStore> store = entityRef.getStore();


        store.getExternalData().getWorld().execute(() -> {

            DeathComponent.respawn(store, entityRef).thenRun(() -> {
            });
        });*/
    }
}
