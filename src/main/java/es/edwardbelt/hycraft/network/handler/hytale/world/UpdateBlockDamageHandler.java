package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.UpdateBlockDamage;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.packet.play.SetBlockDestroyStagePacket;

import java.util.Objects;

public class UpdateBlockDamageHandler implements PacketHandler<UpdateBlockDamage> {
    @Override
    public void handle(UpdateBlockDamage packet, ClientConnection connection) {
        int destroyStage = calculateDestroyStage(packet.damage);

        int sourceId = Objects.hash(packet.blockPosition.x, packet.blockPosition.y, packet.blockPosition.z);

        BlockPosition blockPos = new BlockPosition(packet.blockPosition.x, packet.blockPosition.y, packet.blockPosition.z);

        SetBlockDestroyStagePacket stagePacket = new SetBlockDestroyStagePacket(sourceId, blockPos, destroyStage);
        connection.getChannel().writeAndFlush(stagePacket);
    }

    private int calculateDestroyStage(float damage) {
        if (damage >= 0.95f) return 10;
        if (damage <= 0.0f) return 10;

        float damagePercent = 1.0f - damage;
        int stage = (int)(damagePercent * 10);

        return Math.min(Math.max(stage, 0), 9);
    }
}