package es.edwardbelt.hycraft.mapping.loader;

import com.google.gson.reflect.TypeToken;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.config.JsonConfig;
import es.edwardbelt.hycraft.util.GsonUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class MappingLoader {
    public static Map<String, Object> loadMapping(JsonConfig jsonConfig) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> mappings = GsonUtil.GSON.fromJson(jsonConfig.get(), type);

        if (mappings == null) {
            throw new RuntimeException("Failed to parse mapping file: " + jsonConfig.getPath());
        }

        return mappings;
    }
}