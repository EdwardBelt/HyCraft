package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PoseMetadataValue implements MetadataValue {
    private Pose pose;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(pose.getId());
    }

    @Override
    public int getTypeId() {
        return 20;
    }

    @AllArgsConstructor
    public enum Pose {
        STANDING(0),
        FALL_FLYING(1),
        SLEEPING(2),
        SWIMMING(3),
        SPIN_ATTACK(4),
        SNEAKING(5),
        LONG_JUMPING(6),
        DYING(7);

        @Getter
        private final int id;
    }
}
