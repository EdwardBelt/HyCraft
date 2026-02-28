package es.edwardbelt.hycraft.network.handler.minecraft.manager.blockbreak;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;

import java.util.concurrent.ScheduledFuture;

public class BlockBreakTracker {
    public BlockPosition position;
    public long startTime;
    public ScheduledFuture<?> damageTask;
    public int sequence;

    public BlockBreakTracker(BlockPosition position, int sequence) {
        this.position = position;
        this.sequence = sequence;
        this.startTime = System.currentTimeMillis();
    }

    public void cancel() {
        if (damageTask != null && !damageTask.isDone()) {
            damageTask.cancel(false);
        }
    }
}
