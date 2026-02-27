package es.edwardbelt.hycraft.network.handler.minecraft.data.entity;

import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata.ByteMetadataValue;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata.TextMetadataValue;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata.VarIntMetadataValue;

public class TextEntity extends Entity {
    public TextEntity(int id) {
        super(id);
        this.setType(128);
    }

    public void setText(String text) {
        this.getMetadata().set(23, new TextMetadataValue(text));
    }

    public void setFace(byte face) {
        this.getMetadata().set(15, new ByteMetadataValue(face));
    }

    public void setLineWidth(int width) {
        this.getMetadata().set(24, new VarIntMetadataValue(width));
    }

    public void setBackgroundColor(int a, int r, int g, int b) {
        int color = (a << 24) | (r << 16) | (g << 8) | b;
        this.getMetadata().set(25, new VarIntMetadataValue(color));
    }
}
