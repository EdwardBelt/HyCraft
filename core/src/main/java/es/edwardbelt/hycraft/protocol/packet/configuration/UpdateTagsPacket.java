package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

import java.util.*;

@Getter
public class UpdateTagsPacket implements Packet {

    private Map<String, List<Tag>> taggedRegistries;

    public UpdateTagsPacket() {
        this.taggedRegistries = new HashMap<>();
    }

    public UpdateTagsPacket(Map<String, List<Tag>> taggedRegistries) {
        this.taggedRegistries = taggedRegistries;
    }

    public void addRegistry(String registryId, List<Tag> tags) {
        this.taggedRegistries.put(registryId, tags);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(taggedRegistries.size());

        for (Map.Entry<String, List<Tag>> registry : taggedRegistries.entrySet()) {
            buffer.writeString(registry.getKey());
            buffer.writeVarInt(registry.getValue().size());

            for (Tag tag : registry.getValue()) {
                buffer.writeString(tag.tagName);
                buffer.writeVarInt(tag.entries.length);

                for (int entry : tag.entries) {
                    buffer.writeVarInt(entry);
                }
            }
        }
    }

    public static class Tag {
        public final String tagName;
        public final int[] entries;

        public Tag(String tagName, int... entries) {
            this.tagName = tagName;
            this.entries = entries;
        }
    }

    public static UpdateTagsPacket TAGS_PACKET = createDefault();

    public static UpdateTagsPacket createDefault() {
        UpdateTagsPacket packet = new UpdateTagsPacket();

        List<Tag> timelineTags = new ArrayList<>();
        timelineTags.add(new Tag("minecraft:universal", 3));
        timelineTags.add(new Tag("minecraft:in_nether", 3));
        timelineTags.add(new Tag("minecraft:in_end", 3));
        timelineTags.add(new Tag("minecraft:in_overworld", 3, 0, 2, 1));

        packet.addRegistry("minecraft:timeline", timelineTags);

        return packet;
    }
}