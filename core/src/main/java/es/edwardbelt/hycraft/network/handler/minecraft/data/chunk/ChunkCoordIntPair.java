package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ChunkCoordIntPair {
    private final int x;
    private final int z;
}

