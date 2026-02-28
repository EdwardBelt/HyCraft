package es.edwardbelt.hycraft.mapping.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import es.edwardbelt.hycraft.mapping.Mapper;
import es.edwardbelt.hycraft.util.GsonUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockMapper extends Mapper<Integer> {
    private static final String MINECRAFT_DATA_FILE = "/mappings/blocks_minecraft_data.json";

    private final Map<String, Integer> minecraftDefaultStates = new HashMap<>();

    private JsonObject minecraftData = null;

    public BlockMapper() {
        super("blocks.json");
    }

    @Override
    protected Integer getMappingKeyId(String key) {
        return BlockType.getAssetMap().getIndex(key);
    }

    @Override
    protected void preloadMappingValueIds(Set<String> stringIds) {
        long startTime = System.currentTimeMillis();

        loadMinecraftData();

        if (minecraftData == null) {
            System.err.println("Failed to load Minecraft data, cannot preload IDs");
            return;
        }

        int loaded = 0;
        for (String blockId : stringIds) {
            int stateId = extractDefaultStateId(blockId);
            if (stateId != -1) {
                minecraftDefaultStates.put(blockId, stateId);
                loaded++;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Pre-loaded " + loaded + "/" + stringIds.size() + " Minecraft block states in " + elapsed + "ms");
    }

    @Override
    protected int getMappingValueId(String minecraftStringId) {
        return minecraftDefaultStates.getOrDefault(minecraftStringId, -1);
    }

    @Override
    protected void cleanup() {
        minecraftData = null;
    }

    private void loadMinecraftData() {
        if (minecraftData != null) {
            return;
        }

        try (InputStream inputStream = getClass().getResourceAsStream(MINECRAFT_DATA_FILE)) {
            if (inputStream == null) {
                System.err.println("Minecraft data file not found: " + MINECRAFT_DATA_FILE);
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            minecraftData = GsonUtil.GSON.fromJson(reader, JsonObject.class);

        } catch (Exception e) {
            System.err.println("Failed to load Minecraft data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int extractDefaultStateId(String minecraftStringId) {
        if (!minecraftStringId.contains("minecraft:")) minecraftStringId = "minecraft:" + minecraftStringId;
        if (minecraftData == null || !minecraftData.has(minecraftStringId)) {
            return -1;
        }

        try {
            JsonObject blockData = minecraftData.getAsJsonObject(minecraftStringId);

            if (!blockData.has("states")) {
                return -1;
            }

            JsonArray states = blockData.getAsJsonArray("states");

            for (JsonElement stateElement : states) {
                JsonObject state = stateElement.getAsJsonObject();
                if (state.has("default") && state.get("default").getAsBoolean()) {
                    return state.get("id").getAsInt();
                }
            }

            if (states.size() > 0) {
                JsonObject firstState = states.get(0).getAsJsonObject();
                return firstState.get("id").getAsInt();
            }

        } catch (Exception e) {
            System.err.println("Error extracting state ID for " + minecraftStringId + ": " + e.getMessage());
        }

        return -1;
    }
}