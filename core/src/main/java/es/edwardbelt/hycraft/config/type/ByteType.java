package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonElement;

public class ByteType implements ConfigType<Byte> {
    @Override
    public Byte read(JsonElement json) {
        return json.getAsByte();
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[]{Byte.class, byte.class};
    }
}
