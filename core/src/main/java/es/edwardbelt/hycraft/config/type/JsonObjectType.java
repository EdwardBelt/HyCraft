package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectType implements ConfigType<JsonObject> {
    @Override
    public JsonObject read(JsonElement json) {
        return json.getAsJsonObject();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{JsonObject.class};
    }
}
