package es.edwardbelt.hycraft.mapping.managers;

import com.google.gson.JsonObject;
import es.edwardbelt.hycraft.mapping.Mapper;
import es.edwardbelt.hycraft.util.GsonUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemMapper extends Mapper<String> {
    private static final String MINECRAFT_DATA_FILE = "/mappings/items_minecraft_data.json";

    private final Map<String, Integer> minecraftProtocolIds = new HashMap<>();

    private JsonObject minecraftData = null;

    public ItemMapper() {
        super("items.json");
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
            System.err.println("Failed to load Minecraft data, cannot preload IDs");
            return;
        }

        int loaded = 0;
        for (String itemId : stringIds) {
            int protocolId = extractProtocolId(itemId);
            if (protocolId != -1) {
                minecraftProtocolIds.put(itemId, protocolId);
                loaded++;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Pre-loaded " + loaded + "/" + stringIds.size() + " Minecraft item protocol IDs in " + elapsed + "ms");
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

    private int extractProtocolId(String minecraftStringId) {
        if (!minecraftStringId.contains("minecraft:")) minecraftStringId = "minecraft:" + minecraftStringId;
        if (minecraftData == null || !minecraftData.has(minecraftStringId)) {
            return -1;
        }

        try {
            JsonObject itemData = minecraftData.getAsJsonObject(minecraftStringId);

            if (!itemData.has("protocol_id")) {
                return -1;
            }

            return itemData.get("protocol_id").getAsInt();

        } catch (Exception e) {
            System.err.println("Error extracting protocol ID for " + minecraftStringId + ": " + e.getMessage());
        }

        return -1;
    }
}