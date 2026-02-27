package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ResponseKnownPacksPacket implements Packet {
    private List<KnownPack> knownPacks;

    public ResponseKnownPacksPacket() {
        this.knownPacks = new ArrayList<>();
    }

    public ResponseKnownPacksPacket(String namespace, String id, String version) {
        this();
        this.addKnownPack(namespace, id, version);
    }

    public ResponseKnownPacksPacket(List<KnownPack> knownPacks) {
        this.knownPacks = knownPacks;
    }

    public void addKnownPack(String namespace, String id, String version) {
        this.knownPacks.add(new KnownPack(namespace, id, version));
    }

    @Override
    public void read(PacketBuffer buffer) {
        int count = buffer.readVarInt();
        this.knownPacks = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            String namespace = buffer.readString();
            String id = buffer.readString();
            String version = buffer.readString();

            this.knownPacks.add(new KnownPack(namespace, id, version));
        }
    }

    @Setter
    @Getter
    public static class KnownPack {
        private String namespace;
        private String id;
        private String version;

        public KnownPack(String namespace, String id, String version) {
            this.namespace = namespace;
            this.id = id;
            this.version = version;
        }

    }
}