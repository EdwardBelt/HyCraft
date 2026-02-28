package es.edwardbelt.hycraft.network.handler.minecraft.data.profile;

import lombok.Getter;

@Getter
public class Property {
    private final String name;
    private final String value;
    private final String signature;

    public Property(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public boolean isSigned() {
        return signature != null;
    }
}