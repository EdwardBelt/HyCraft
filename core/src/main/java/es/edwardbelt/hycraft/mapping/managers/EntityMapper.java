package es.edwardbelt.hycraft.mapping.managers;

import com.google.gson.JsonObject;
import es.edwardbelt.hycraft.mapping.Mapper;
import es.edwardbelt.hycraft.util.GsonUtil;
import es.edwardbelt.hycraft.util.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityMapper extends Mapper<String> {
    private static final String MINECRAFT_DATA_FILE = "/mappings/entities_minecraft_data.json";

    private final Map<String, Integer> minecraftProtocolIds = new HashMap<>();

    private JsonObject minecraftData = null;

    public EntityMapper() {
        super("entities.json");
    }

    @Override
    protected String getMappingKeyId(String key) {
        return key;
    }

    @Override
    protected void preloadMappingValueIds(Set<String> stringIds) {
        long startTime = System.currentTimeMillis();
        loadMinecraftData();

        if (minecraftData == null) {
            Logger.ERROR.log("Failed to load Minecraft data, cannot preload IDs");
            return;
        }

        int loaded = 0;
        for (String entityId : stringIds) {
            int protocolId = extractProtocolId(entityId);
            if (protocolId != -1) {
                minecraftProtocolIds.put(entityId, protocolId);
                loaded++;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        Logger.INFO.log("Pre-loaded " + loaded + "/" + stringIds.size() + " Minecraft entity protocol IDs in " + elapsed + "ms");
    }

    @Override
    protected int getMappingValueId(String minecraftStringId) {
        return minecraftProtocolIds.getOrDefault(minecraftStringId, -1);
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
                Logger.ERROR.log("Minecraft data file not found: " + MINECRAFT_DATA_FILE);
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            minecraftData = GsonUtil.GSON.fromJson(reader, JsonObject.class);

        } catch (Exception e) {
            Logger.ERROR.log("Failed to load Minecraft data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int extractProtocolId(String minecraftStringId) {
        if (!minecraftStringId.contains("minecraft:")) minecraftStringId = "minecraft:" + minecraftStringId;
        if (minecraftData == null || !minecraftData.has(minecraftStringId)) {
            return -1;
        }

        try {
            JsonObject entityData = minecraftData.getAsJsonObject(minecraftStringId);

            if (!entityData.has("protocol_id")) {
                return -1;
            }

            return entityData.get("protocol_id").getAsInt();

        } catch (Exception e) {
            Logger.ERROR.log("Error extracting protocol ID for " + minecraftStringId + ": " + e.getMessage());
        }

        return -1;
    }
}