package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal;

import com.hypixel.hytale.server.core.HytaleServer;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class Goal {
    public static final long TICK_TIME = 25;

    private ScheduledFuture task;
    @Setter
    private Runnable startRunnable;
    @Setter
    private Runnable endRunnable;
    @Setter
    private Runnable eachTickRunnable;
    @Getter
    @Setter
    private Entity entity;
    @Getter
    @Setter
    private ClientConnection connection;

    public void init() {
        if (startRunnable != null) startRunnable.run();
        start();

        task = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
            if (shouldExecute()) {
                if (eachTickRunnable != null) eachTickRunnable.run();
                tick();
            } else {
                if (endRunnable != null) {
                    endRunnable.run();
                }

                task.cancel(false);
            }
        }, 0, TICK_TIME, TimeUnit.MILLISECONDS);
    }

    public boolean isRunning() {
        return task != null && !task.isCancelled();
    }

    public abstract void start();
    public abstract boolean shouldExecute();
    public abstract void tick();
}
