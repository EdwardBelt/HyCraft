package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistryDataPacket implements Packet {

    private String registryId;
    private List<RegistryEntry> entries;

    public RegistryDataPacket() {
        this.registryId = "minecraft:dimension_type";
        this.entries = createDefaultDimensionTypes();
    }

    public RegistryDataPacket(String registryId, List<RegistryEntry> entries) {
        this.registryId = registryId;
        this.entries = entries;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(registryId);
        buffer.writeVarInt(entries.size());

        for (RegistryEntry entry : entries) {
            buffer.writeString(entry.id);
            buffer.writeBoolean(false);
            /*buffer.writeBoolean(entry.data != null);

            if (entry.data != null) {
                //buffer.writePrefixedOptionalNBT(entry.data);
            }*/
        }
    }

    public static class RegistryEntry {
        public final String id;

        public RegistryEntry(String id) {
            this.id = id;
        }
    }

    public static List<RegistryDataPacket> DEFAULT_REGISTRIES = createDefaultRegistries();

    private static List<RegistryDataPacket> createDefaultRegistries() {
        List<RegistryDataPacket> packets = new ArrayList<>();

        packets.add(new RegistryDataPacket("minecraft:banner_pattern", createDefaultBannerPatterns()));
        packets.add(new RegistryDataPacket("minecraft:chat_type", createDefaultChatTypes()));
        packets.add(new RegistryDataPacket("minecraft:damage_type", createDefaultDamageTypes()));
        packets.add(new RegistryDataPacket("minecraft:dimension_type", createDefaultDimensionTypes()));
        packets.add(new RegistryDataPacket("minecraft:worldgen/biome", createDefaultBiomes()));
        packets.add(new RegistryDataPacket("minecraft:trim_pattern", createDefaultTrimPatterns()));
        packets.add(new RegistryDataPacket("minecraft:trim_material", createDefaultTrimMaterials()));
        packets.add(new RegistryDataPacket("minecraft:wolf_variant", createDefaultWolfVariants()));
        packets.add(new RegistryDataPacket("minecraft:wolf_sound_variant", createDefaultWolfSoundVariants()));
        packets.add(new RegistryDataPacket("minecraft:painting_variant", createDefaultPaintingVariants()));
        //packets.add(new RegistryDataPacket("minecraft:enchantment", createDefaultEnchantments()));
        packets.add(new RegistryDataPacket("minecraft:jukebox_song", createDefaultJukeboxSongs()));
        packets.add(new RegistryDataPacket("minecraft:instrument", createDefaultInstruments()));
        packets.add(new RegistryDataPacket("minecraft:pig_variant", createDefaultPigVariants()));
        packets.add(new RegistryDataPacket("minecraft:frog_variant", createDefaultFrogVariants()));
        packets.add(new RegistryDataPacket("minecraft:cat_variant", createDefaultCatVariants()));
        packets.add(new RegistryDataPacket("minecraft:cow_variant", createDefaultCowVariants()));
        packets.add(new RegistryDataPacket("minecraft:chicken_variant", createDefaultChickenVariants()));
        //packets.add(new RegistryDataPacket("minecraft:dialog", createDefaultDialogs()));

        return packets;
    }

    private static List<RegistryEntry> createDefaultDimensionTypes() {
        List<RegistryEntry> entries = new ArrayList<>();

        entries.add(new RegistryEntry("minecraft:overworld"));
        entries.add(new RegistryEntry("minecraft:overworld_caves"));
        entries.add(new RegistryEntry("minecraft:the_nether"));
        entries.add(new RegistryEntry("minecraft:the_end"));

        return entries;
    }

    private static List<RegistryEntry> createDefaultBiomes() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] biomes = {
                "badlands", "bamboo_jungle", "basalt_deltas", "beach", "birch_forest",
                "cherry_grove", "cold_ocean", "crimson_forest", "dark_forest", "deep_cold_ocean",
                "deep_dark", "deep_frozen_ocean", "deep_lukewarm_ocean", "deep_ocean", "desert",
                "dripstone_caves", "end_barrens", "end_highlands", "end_midlands", "eroded_badlands",
                "flower_forest", "forest", "frozen_ocean", "frozen_peaks", "frozen_river",
                "grove", "ice_spikes", "jagged_peaks", "jungle", "lukewarm_ocean",
                "lush_caves", "mangrove_swamp", "meadow", "mushroom_fields", "nether_wastes",
                "ocean", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga", "pale_garden",
                "plains", "river", "savanna", "savanna_plateau", "small_end_islands",
                "snowy_beach", "snowy_plains", "snowy_slopes", "snowy_taiga", "soul_sand_valley",
                "sparse_jungle", "stony_peaks", "stony_shore", "sunflower_plains", "swamp",
                "taiga", "the_end", "the_void", "warm_ocean", "warped_forest",
                "windswept_forest", "windswept_gravelly_hills", "windswept_hills", "windswept_savanna", "wooded_badlands"
        };

        for (String biomeName : biomes) {
            entries.add(new RegistryEntry("minecraft:" + biomeName));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultDamageTypes() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] damageTypes = {
                "arrow", "bad_respawn_point", "cactus", "campfire", "cramming",
                "dragon_breath", "drown", "dry_out", "ender_pearl", "explosion",
                "fall", "falling_anvil", "falling_block", "falling_stalactite", "fireball",
                "fireworks", "fly_into_wall", "freeze", "generic", "generic_kill",
                "hot_floor", "in_fire", "in_wall", "indirect_magic", "lava",
                "lightning_bolt", "mace_smash", "magic", "mob_attack", "mob_attack_no_aggro",
                "mob_projectile", "on_fire", "out_of_world", "outside_border", "player_attack",
                "player_explosion", "sonic_boom", "spit", "stalagmite", "starve",
                "sting", "sweet_berry_bush", "thorns", "thrown", "trident",
                "unattributed_fireball", "wind_charge", "wither", "wither_skull"
        };

        for (String type : damageTypes) {
            entries.add(new RegistryEntry("minecraft:" + type));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultChatTypes() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] chatTypes = {
                "chat", "emote_command", "msg_command_incoming", "msg_command_outgoing",
                "say_command", "team_msg_command_incoming", "team_msg_command_outgoing"
        };

        for (String chatType : chatTypes) {
            entries.add(new RegistryEntry("minecraft:" + chatType));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultTrimPatterns() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] patterns = {
                "bolt", "coast", "dune", "eye", "flow", "host", "raiser", "rib",
                "sentry", "shaper", "silence", "snout", "spire", "tide", "vex",
                "ward", "wayfinder", "wild"
        };

        for (String pattern : patterns) {
            entries.add(new RegistryEntry("minecraft:" + pattern));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultTrimMaterials() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] materials = {
                "amethyst", "copper", "diamond", "emerald", "gold", "iron",
                "lapis", "netherite", "quartz", "redstone", "resin"
        };

        for (String material : materials) {
            entries.add(new RegistryEntry("minecraft:" + material));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultWolfVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "ashen", "black", "chestnut", "pale", "rusty",
                "snowy", "spotted", "striped", "woods"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultWolfSoundVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "angry", "big", "classic", "cute", "grumpy",
                "puglin", "sad"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultPaintingVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] paintings = {
                "alban", "aztec", "aztec2", "backyard", "baroque", "bomb",
                "bouquet", "burning_skull", "bust", "cavebird", "changing", "cotan",
                "courbet", "creebet", "dennis", "donkey_kong", "earth", "endboss", "fern",
                "fighters", "finding", "fire", "graham", "humble", "kebab",
                "lowmist", "match", "meditative", "orb", "owlemons", "passage",
                "pigscene", "plant", "pointer", "pond", "pool", "prairie_ride",
                "sea", "skeleton", "skull_and_roses", "stage", "sunflowers", "sunset",
                "tides", "unpacked", "void", "wanderer", "wasteland", "water",
                "wind", "wither"
        };

        for (String painting : paintings) {
            entries.add(new RegistryEntry("minecraft:" + painting));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultBannerPatterns() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] patterns = {
                "base", "border", "bricks", "circle", "creeper", "cross", "curly_border",
                "diagonal_left", "diagonal_right", "diagonal_up_left", "diagonal_up_right",
                "flow", "flower", "globe", "gradient", "gradient_up", "guster",
                "half_horizontal", "half_horizontal_bottom", "half_vertical", "half_vertical_right",
                "mojang", "piglin", "rhombus", "skull", "small_stripes",
                "square_bottom_left", "square_bottom_right", "square_top_left", "square_top_right",
                "straight_cross", "stripe_bottom", "stripe_center", "stripe_downleft",
                "stripe_downright", "stripe_left", "stripe_middle", "stripe_right", "stripe_top",
                "triangle_bottom", "triangle_top", "triangles_bottom", "triangles_top"
        };

        for (String pattern : patterns) {
            entries.add(new RegistryEntry("minecraft:" + pattern));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultEnchantments() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] enchantments = {
                "aqua_affinity", "bane_of_arthropods", "binding_curse", "blast_protection",
                "breach", "channeling", "density", "depth_strider", "efficiency",
                "feather_falling", "fire_aspect", "fire_protection", "flame", "fortune",
                "frost_walker", "impaling", "infinity", "knockback", "looting",
                "loyalty", "luck_of_the_sea", "lure", "mending", "multishot",
                "piercing", "power", "projectile_protection", "protection", "punch",
                "quick_charge", "respiration", "riptide", "sharpness", "silk_touch",
                "smite", "soul_speed", "sweeping_edge", "swift_sneak", "thorns",
                "unbreaking", "vanishing_curse", "wind_burst"
        };

        for (String enchantment : enchantments) {
            entries.add(new RegistryEntry("minecraft:" + enchantment));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultJukeboxSongs() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] songs = {
                "11", "13", "5", "blocks", "cat", "chirp", "creator", "creator_music_box",
                "far", "lava_chicken", "mall", "mellohi", "otherside", "pigstep", "precipice",
                "relic", "stal", "strad", "tears", "wait", "ward"
        };

        for (String song : songs) {
            entries.add(new RegistryEntry("minecraft:" + song));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultInstruments() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] instruments = {
                "admire_goat_horn", "call_goat_horn", "dream_goat_horn", "feel_goat_horn",
                "ponder_goat_horn", "seek_goat_horn", "sing_goat_horn", "yearn_goat_horn"
        };

        for (String instrument : instruments) {
            entries.add(new RegistryEntry("minecraft:" + instrument));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultPigVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "cold", "temperate", "warm"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultFrogVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "cold", "temperate", "warm"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultCatVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "all_black", "black", "british_shorthair", "calico", "jellie",
                "persian", "ragdoll", "red", "siamese", "tabby", "white"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultCowVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "cold", "temperate", "warm"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultChickenVariants() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] variants = {
                "cold", "temperate", "warm"
        };

        for (String variant : variants) {
            entries.add(new RegistryEntry("minecraft:" + variant));
        }

        return entries;
    }

    private static List<RegistryEntry> createDefaultDialogs() {
        List<RegistryEntry> entries = new ArrayList<>();

        String[] dialogs = {
                "custom_options", "quick_actions", "server_links"
        };

        for (String dialog : dialogs) {
            entries.add(new RegistryEntry("minecraft:" + dialog));
        }

        return entries;
    }
}