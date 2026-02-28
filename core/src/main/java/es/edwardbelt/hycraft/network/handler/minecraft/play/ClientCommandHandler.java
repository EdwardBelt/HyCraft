package es.edwardbelt.hycraft.network.handler.minecraft.play;

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
