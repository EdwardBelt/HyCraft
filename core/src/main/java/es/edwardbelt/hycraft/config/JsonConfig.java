package es.edwardbelt.hycraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import es.edwardbelt.hycraft.HyCraft;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;

public class JsonConfig {
    @Getter
    private final String path;
    private final File configFile;
    private JsonObject config;

    public JsonConfig(String path) {
        if (!path.endsWith(".json")) path = path + ".json";
        this.path = path;
        this.configFile = new File(HyCraft.PATH, path);
        load();
    }

    public JsonObject get() {
        return config;
    }

    public void load() {
        if (!configFile.exists()) {
            saveDefaults();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            config = new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaults() {
        configFile.getParentFile().mkdirs();

        try (InputStream in = getClass().getResourceAsStream("/" + path)) {
            if (in == null) {
                config = new JsonObject();
                save();
                return;
            }
            Files.copy(in, configFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract default config: " + path, e);
        }

        try (FileReader reader = new FileReader(configFile)) {
            config = new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default config: " + path, e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
