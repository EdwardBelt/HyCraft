package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class BooleanType implements ConfigType<Boolean> {
    @Override
    public Boolean read(JsonElement json) {
        return json.getAsBoolean();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{Boolean.class, boolean.class};
    }
}
