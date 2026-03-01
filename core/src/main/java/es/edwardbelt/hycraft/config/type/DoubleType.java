package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class DoubleType implements ConfigType<Double> {
    @Override
    public Double read(JsonElement json) {
        return json.getAsDouble();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{Double.class, double.class};
    }
}
