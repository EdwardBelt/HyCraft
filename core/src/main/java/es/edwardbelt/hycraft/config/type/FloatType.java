package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class FloatType implements ConfigType<Float> {
    @Override
    public Float read(JsonElement json) {
        return json.getAsFloat();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{Float.class, float.class};
    }
}
