package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public interface ConfigType<T> {
    T read(JsonElement json);
    Class<?>[] supportedTypes();
}
