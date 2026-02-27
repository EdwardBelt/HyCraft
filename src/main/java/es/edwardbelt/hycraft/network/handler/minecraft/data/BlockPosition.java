package es.edwardbelt.hycraft.network.handler.minecraft.data;

import com.hypixel.hytale.math.vector.Vector3i;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BlockPosition {
    private final int x;
    private final int y;
    private final int z;

    public static BlockPosition fromLong(long encoded) {
        int x = (int)(encoded >> 38);
        int y = (int)(encoded << 52 >> 52);
        int z = (int)(encoded << 26 >> 38);
        return new BlockPosition(x, y, z);
    }
    public long toLong() {
        return ((long)x & 0x3FFFFFF) << 38 | ((long)z & 0x3FFFFFF) << 12 | ((long)y & 0xFFF);
    }

    public BlockPosition offset(int dx, int dy, int dz) {
        return new BlockPosition(x + dx, y + dy, z + dz);
    }

    public double distanceSquared(BlockPosition other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public Vector3i toVector3i() {
        return new Vector3i(x, y, z);
    }

    public static BlockPosition fromVector3i(Vector3i vector) {
        return new BlockPosition(vector.x, vector.y, vector.z);
    }

    public double distance(BlockPosition other) {
        return Math.sqrt(distanceSquared(other));
    }
}