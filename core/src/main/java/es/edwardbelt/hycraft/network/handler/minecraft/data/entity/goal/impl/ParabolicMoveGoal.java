package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.impl;

import com.hypixel.hytale.math.vector.Vector3d;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.Goal;

public class ParabolicMoveGoal extends Goal {
    private final double startX;
    private final double startY;
    private final double startZ;
    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final double height;
    private final long timeToComplete;

    private int currentTick;
    private int totalTicks;

    private boolean shouldExecute;

    public ParabolicMoveGoal(Vector3d start, Vector3d end, double height, long timeToComplete) {
        this.startX = start.getX();
        this.startY = start.getY();
        this.startZ = start.getZ();
        this.targetX = end.getX();
        this.targetY = end.getY();
        this.targetZ = end.getZ();
        this.height = height;
        this.timeToComplete = timeToComplete;
        this.shouldExecute = true;
    }

    @Override
    public boolean shouldExecute() {
        return shouldExecute;
    }

    @Override
    public void start() {
        this.currentTick = 0;
        this.totalTicks = (int) Math.max(1, timeToComplete / TICK_TIME);
    }

    @Override
    public void tick() {
        currentTick++;

        if (currentTick >= totalTicks) {
            getEntity().move(getConnection(), targetX, targetY, targetZ);
            shouldExecute = false;
            return;
        }

        double t = (double) currentTick / totalTicks;

        double interpolatedX = startX + (targetX - startX) * t;
        double interpolatedZ = startZ + (targetZ - startZ) * t;
        double interpolatedY = startY + (targetY - startY) * t + height * 4 * t * (1 - t);

        getEntity().move(getConnection(), interpolatedX, interpolatedY, interpolatedZ);
    }
}