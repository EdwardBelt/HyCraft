package es.edwardbelt.hycraft.mapping;

import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.config.JsonConfig;
import es.edwardbelt.hycraft.mapping.loader.MappingLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Mapper<K> {
    private final static String UNMAPPED_VALUE_ID = "Unmapped_Value";

    private final Map<K, Integer> mappings = new HashMap<>();
    private int unmappedValue;
    private final String fileName;
    private final JsonConfig jsonConfig;

    private final Set<String> neededStringIds = new HashSet<>();

    public Mapper(String fileName) {
        this.fileName = fileName;
        this.jsonConfig = new JsonConfig("mappings/" + fileName);
    }

    public void loadMappings() {
        try {
            Map<String, Object> rawMappings = MappingLoader.loadMapping(jsonConfig);
            mappings.clear();
            neededStringIds.clear();

            for (Map.Entry<String, Object> entry : rawMappings.entrySet()) {
                Object mappingValue = entry.getValue();

                if (mappingValue instanceof String) {
                    neededStringIds.add((String) mappingValue);
                }
            }

            if (!neededStringIds.isEmpty()) {
                System.out.println("Pre-loading " + neededStringIds.size() + " Minecraft mappings...");
                preloadMappingValueIds(neededStringIds);
            }

            for (Map.Entry<String, Object> entry : rawMappings.entrySet()) {
                String mappingKey = entry.getKey();
                Object mappingValue = entry.getValue();

                int mappingValueId;
                if (mappingValue instanceof Number) {
                    mappingValueId = ((Number) mappingValue).intValue();
                } else if (mappingValue instanceof String stringId) {
                    mappingValueId = getMappingValueId(stringId);
                    if (mappingValueId == -1) {
                        System.out.println("Invalid value id for " + stringId);
                        continue;
                    }
                } else {
                    System.out.println("Unknown value type for " + mappingKey + ": " + mappingValue.getClass().getName());
                    continue;
                }

                if (mappingKey.equalsIgnoreCase(UNMAPPED_VALUE_ID)) {
                    this.unmappedValue = mappingValueId;
                    continue;
                }

                K mappingKeyId = getMappingKeyId(mappingKey);
                if (mappingKeyId == null) {
                    System.out.println("Invalid key id for " + mappingKey);
                    continue;
                }

                mappings.put(mappingKeyId, mappingValueId);
            }

            System.out.println("Loaded " + mappings.size() + " block mappings");

            cleanup();

        } catch (Exception e) {
            System.out.println("Failed to load block mappings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getMapping(K key) {
        return mappings.getOrDefault(key, unmappedValue);
    }

    protected abstract void preloadMappingValueIds(Set<String> stringIds);
    protected abstract int getMappingValueId(String key);
    protected abstract K getMappingKeyId(String key);
    protected abstract void cleanup();
}