package es.edwardbelt.hycraft.mapping.managers;

import com.google.gson.JsonObject;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import es.edwardbelt.hycraft.mapping.Mapper;
import es.edwardbelt.hycraft.util.GsonUtil;
import es.edwardbelt.hycraft.util.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoundMapper extends Mapper<Integer> {
    private static final String MINECRAFT_DATA_FILE = "/mappings/sounds_minecraft_data.json";

    private final Map<String, Integer> minecraftSoundIds = new HashMap<>();

    private JsonObject minecraftData = null;

    public SoundMapper() {
        super("sounds.json");
    }

    @Override
    protected Integer getMappingKeyId(String key) {
        return SoundEvent.getAssetMap().getIndex(key);
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
        for (String soundId : stringIds) {
            int protocolId = extractSoundProtocolId(soundId);
            if (protocolId != -1) {
                minecraftSoundIds.put(soundId, protocolId);
                loaded++;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        Logger.INFO.log("Pre-loaded " + loaded + "/" + stringIds.size() + " Minecraft sound IDs in " + elapsed + "ms");
    }

    @Override
    protected int getMappingValueId(String minecraftStringId) {
        return minecraftSoundIds.getOrDefault(minecraftStringId, -1);
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

    private int extractSoundProtocolId(String minecraftStringId) {
        if (!minecraftStringId.contains("minecraft:")) {
            minecraftStringId = "minecraft:" + minecraftStringId;
        }

        if (minecraftData == null || !minecraftData.has(minecraftStringId)) {
            return -1;
        }

        try {
            JsonObject soundData = minecraftData.getAsJsonObject(minecraftStringId);

            if (!soundData.has("protocol_id")) {
                return -1;
            }

            return soundData.get("protocol_id").getAsInt();

        } catch (Exception e) {
            Logger.ERROR.log("Error extracting protocol ID for " + minecraftStringId + ": " + e.getMessage());
        }

        return -1;
    }
}