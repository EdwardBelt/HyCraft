package es.edwardbelt.hycraft.network.handler.hytale;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.connection.Ping;
import com.hypixel.hytale.protocol.packets.entities.ApplyKnockback;
import com.hypixel.hytale.protocol.packets.entities.ChangeVelocity;
import com.hypixel.hytale.protocol.packets.entities.EntityUpdates;
import com.hypixel.hytale.protocol.packets.entities.PlayAnimation;
import com.hypixel.hytale.protocol.packets.interaction.PlayInteractionFor;
import com.hypixel.hytale.protocol.packets.interface_.*;
import com.hypixel.hytale.protocol.packets.inventory.SetActiveSlot;
import com.hypixel.hytale.protocol.packets.inventory.UpdatePlayerInventory;
import com.hypixel.hytale.protocol.packets.player.*;
import com.hypixel.hytale.protocol.packets.world.*;
import es.edwardbelt.hycraft.network.handler.HandlerRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.connection.PingHandler;
import es.edwardbelt.hycraft.network.handler.hytale.connection.SetClientIdHandler;
import es.edwardbelt.hycraft.network.handler.hytale.entities.ApplyKnockbackHandler;
import es.edwardbelt.hycraft.network.handler.hytale.entities.ChangeVelocityHandler;
import es.edwardbelt.hycraft.network.handler.hytale.entities.EntityUpdatesHandler;
import es.edwardbelt.hycraft.network.handler.hytale.entities.PlayAnimationHandler;
import es.edwardbelt.hycraft.network.handler.hytale.interaction.PlayInteractionForHandler;
import es.edwardbelt.hycraft.network.handler.hytale.interface_.*;
import es.edwardbelt.hycraft.network.handler.hytale.inventory.SetActiveSlotHandler;
import es.edwardbelt.hycraft.network.handler.hytale.inventory.UpdatePlayerInventoryHandler;
import es.edwardbelt.hycraft.network.handler.hytale.player.ClientTeleportHandler;
import es.edwardbelt.hycraft.network.handler.hytale.player.SetGameModeHandler;
import es.edwardbelt.hycraft.network.handler.hytale.player.SetMovementStatesHandler;
import es.edwardbelt.hycraft.network.handler.hytale.world.*;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class HytaleHandlerRegistry extends HandlerRegistry<Packet> {

    public HytaleHandlerRegistry() {
        // connection
        this.addHandler(SetClientId.class, new SetClientIdHandler());
        this.addHandler(Ping.class, new PingHandler());

        // entities
        this.addHandler(EntityUpdates.class, new EntityUpdatesHandler());
        this.addHandler(PlayAnimation.class, new PlayAnimationHandler());
        this.addHandler(ApplyKnockback.class, new ApplyKnockbackHandler());
        this.addHandler(ChangeVelocity.class, new ChangeVelocityHandler());

        // interface
        this.addHandler(AddToServerPlayerList.class, new AddToServerPlayerListHandler());
        this.addHandler(UpdateServerPlayerList.class, new UpdateServerPlayerListHandler());
        this.addHandler(RemoveFromServerPlayerList.class, new RemoveFromServerPlayerListHandler());
        this.addHandler(UpdateServerPlayerListPing.class, new UpdateServerPlayerListPingHandler());
        this.addHandler(ServerInfo.class, new ServerInfoHandler());
        this.addHandler(Notification.class, new NotificationHandler());
        this.addHandler(ServerMessage.class, new ServerMessageHandler());
        this.addHandler(ShowEventTitle.class, new ShowEventTitleHandler());

        // interaction
        this.addHandler(PlayInteractionFor.class, new PlayInteractionForHandler());

        // inventory
        this.addHandler(UpdatePlayerInventory.class, new UpdatePlayerInventoryHandler());
        this.addHandler(SetActiveSlot.class, new SetActiveSlotHandler());

        // player
        this.addHandler(SetGameMode.class, new SetGameModeHandler());
        this.addHandler(SetMovementStates.class, new SetMovementStatesHandler());
        this.addHandler(ClientTeleport.class, new ClientTeleportHandler());

        // world
        this.addHandler(JoinWorld.class, new JoinWorldHandler());
        this.addHandler(SetChunk.class, new SetChunkHandler());
        this.addHandler(UnloadChunk.class, new UnloadChunkHandler());
        this.addHandler(ServerSetBlock.class, new ServerSetBlockHandler());
        this.addHandler(ServerSetBlocks.class, new ServerSetBlocksHandler());
        this.addHandler(UpdateBlockDamage.class, new UpdateBlockDamageHandler());
        this.addHandler(UpdateTime.class, new UpdateTimeHandler());
        this.addHandler(PlaySoundEvent2D.class, new PlaySound2DHandler());
        this.addHandler(PlaySoundEvent3D.class, new PlaySound3DHandler());
        this.addHandler(SpawnParticleSystem.class, new SpawnParticleHandler());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handlePacket(Packet packet, ClientConnection connection) {
        Class<? extends Packet> packetType = HytaleUtil.getPacketClazz(packet);
        if (!hasHandler(packetType)) {
            return;
        }

        packet = HytaleUtil.transformPacket(packet);

        PacketHandler<Packet> handler = (PacketHandler<Packet>) getPacketHandler(packetType);
        this.handle(handler, packet, connection);
    }
}
