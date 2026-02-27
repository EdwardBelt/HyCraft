package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTimePacket implements Packet {
    private long worldAge;
    private long timeOfDay;
    private boolean timeOfDayIncreasing;

    public UpdateTimePacket(long worldAge, long timeOfDay, boolean timeOfDayIncreasing) {
        this.worldAge = worldAge;
        this.timeOfDay = timeOfDay;
        this.timeOfDayIncreasing = timeOfDayIncreasing;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(worldAge);
        buffer.writeLong(timeOfDay);
        buffer.writeBoolean(timeOfDayIncreasing);
    }
}