package es.edwardbelt.hycraft.mapping;

import es.edwardbelt.hycraft.mapping.managers.BlockMapper;
import es.edwardbelt.hycraft.mapping.managers.EntityMapper;
import es.edwardbelt.hycraft.mapping.managers.ItemMapper;
import es.edwardbelt.hycraft.mapping.managers.SoundMapper;
import lombok.Getter;

@Getter
public class MappingRegistry {
    private static MappingRegistry INSTANCE;

    private final BlockMapper blockMapper;
    private final ItemMapper itemMapper;
    private final EntityMapper entityMapper;
    private final SoundMapper soundMapper;

    public MappingRegistry() {
        this.blockMapper = new BlockMapper();
        this.itemMapper = new ItemMapper();
        this.entityMapper = new EntityMapper();
        this.soundMapper = new SoundMapper();
        this.blockMapper.loadMappings();
        this.itemMapper.loadMappings();
        this.entityMapper.loadMappings();
        this.soundMapper.loadMappings();
    }

    public static void init() {
        INSTANCE = new MappingRegistry();
    }

    public static MappingRegistry get() {
        return INSTANCE;
    }
}