package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class StringType implements ConfigType<String> {
    @Override
    public String read(JsonElement json) {
        return json.getAsString();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{String.class};
    }
}
