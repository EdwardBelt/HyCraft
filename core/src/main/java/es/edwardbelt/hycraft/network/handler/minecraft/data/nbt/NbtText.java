package es.edwardbelt.hycraft.network.handler.minecraft.data.nbt;

import java.util.ArrayList;

public class NbtText extends NbtCompound {
    public void setText(String text) {
        setString("text", text);
    }

    public void setColor(String color) {
        setString("color", color);
    }

    public void setBold(boolean bold) {
        setBoolean("bold", bold);
    }

    public void setItalic(boolean italic) {
        setBoolean("italic", italic);
    }

    public void setUnderlined(boolean underlined) {
        setBoolean("underlined", underlined);
    }

    public void addChildren(NbtText text) {
        if (!hasTag("extra")) setList("extra", new ArrayList<>());
        NbtList extra = getList("extra");
        extra.addTag(text);
    }
}
