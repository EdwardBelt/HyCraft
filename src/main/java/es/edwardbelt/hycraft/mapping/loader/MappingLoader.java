package es.edwardbelt.hycraft.mapping.loader;

import com.google.gson.reflect.TypeToken;
import es.edwardbelt.hycraft.util.GsonUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MappingLoader {
    public static Map<String, Object> loadMapping(String fileName) {
        String path = "/mappings/" + fileName;

        try (InputStream inputStream = MappingLoader.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("Mapping file not found: " + path);
            }

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> mappings = GsonUtil.GSON.fromJson(reader, type);

            if (mappings == null) {
                throw new RuntimeException("Failed to parse mapping file: " + path);
            }

            return mappings;
        } catch (Exception e) {
            throw new RuntimeException("Error loading mapping file: " + path, e);
        }
    }

    public static boolean mappingExists(String fileName) {
        String path = "/mappings/" + fileName;
        try (InputStream inputStream = MappingLoader.class.getResourceAsStream(path)) {
            return inputStream != null;
        } catch (Exception e) {
            return false;
        }
    }
}