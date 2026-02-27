package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SendKnownPacksPacket implements Packet {
    private List<KnownPack> knownPacks;

    public SendKnownPacksPacket() {
        this.knownPacks = new ArrayList<>();
    }

    public SendKnownPacksPacket(String namespace, String id, String version) {
        this();
        this.addKnownPack(namespace, id, version);
    }

    public SendKnownPacksPacket(List<KnownPack> knownPacks) {
        this.knownPacks = knownPacks;
    }

    public void addKnownPack(String namespace, String id, String version) {
        this.knownPacks.add(new KnownPack(namespace, id, version));
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(knownPacks.size());
        for (KnownPack pack : knownPacks) {
            buffer.writeString(pack.getNamespace());
            buffer.writeString(pack.getId());
            buffer.writeString(pack.getVersion());
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