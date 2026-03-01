package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class IntegerType implements ConfigType<Integer> {
    @Override
    public Integer read(JsonElement json) {
        return json.getAsInt();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{Integer.class, int.class};
    }
}
