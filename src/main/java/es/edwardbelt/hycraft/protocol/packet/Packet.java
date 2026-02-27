package es.edwardbelt.hycraft.protocol.packet;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public interface Packet {
    default void read(PacketBuffer buffer) {};
    default void write(PacketBuffer buffer) {};
}