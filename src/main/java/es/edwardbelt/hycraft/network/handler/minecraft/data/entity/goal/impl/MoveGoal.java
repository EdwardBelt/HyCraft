package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.impl;

import com.hypixel.hytale.math.vector.Vector3d;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.Goal;
import lombok.Setter;

public class MoveGoal extends Goal {
    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final long timeToComplete;

    private double startX;
    private double startY;
    private double startZ;

    private int currentTick;
    private int totalTicks;

    private boolean shouldExecute;

    @Setter
    private boolean sendRotationEachTick;
    @Setter
    private boolean invertRotation;
    @Setter
    private boolean affectRotation;

    private float yaw;
    private float pitch;

    public MoveGoal(Vector3d moveGoal, long timeToComplete) {
        this.targetX = moveGoal.getX();
        this.targetY = moveGoal.getY();
        this.targetZ = moveGoal.getZ();
        this.timeToComplete = timeToComplete;
        this.shouldExecute = true;
        this.invertRotation = false;
        this.sendRotationEachTick = false;
    }

    @Override
    public boolean shouldExecute() {
        return shouldExecute;
    }

    @Override
    public void start() {
        this.startX = getEntity().getX();
        this.startY = getEntity().getY();
        this.startZ = getEntity().getZ();
        this.currentTick = 0;
        this.totalTicks = (int) Math.max(1, timeToComplete / TICK_TIME);

        if (affectRotation) {
            getMoveGoalYaw();
            getLookAtPitch();
            getEntity().rotate(getConnection(), yaw, pitch);
            getEntity().rotateHead(getConnection(), yaw);
        }
    }

    @Override
    public void tick() {
        currentTick++;

        if (currentTick >= totalTicks) {
            getEntity().move(getConnection(), targetX, targetY, targetZ);
            if (sendRotationEachTick) {
                getEntity().rotate(getConnection(), yaw, pitch);
                getEntity().rotateHead(getConnection(), yaw);
            }
            shouldExecute = false;
            return;
        }

        double t = (double) currentTick / totalTicks;

        double interpolatedX = startX + (targetX - startX) * t;
        double interpolatedY = startY + (targetY - startY) * t;
        double interpolatedZ = startZ + (targetZ - startZ) * t;

        getEntity().move(getConnection(), interpolatedX, interpolatedY, interpolatedZ);

        if (sendRotationEachTick) {
            getEntity().rotate(getConnection(), yaw, pitch);
            getEntity().rotateHead(getConnection(), yaw);
        }
    }

    private float getMoveGoalYaw() {
        double dx = targetX - getEntity().getX();
        double dz = targetZ - getEntity().getZ();

        float yaw;
        if (invertRotation) {
            yaw = (float) (Math.atan2(dx, -dz) * 180 / Math.PI);
        } else {
            yaw = (float) (Math.atan2(-dx, dz) * 180 / Math.PI);
        }

        this.yaw = yaw;
        return yaw;
    }

    private float getLookAtPitch() {
        double dx = targetX - getEntity().getX();
        double dy = targetY - getEntity().getY();
        double dz = targetZ - getEntity().getZ();

        double r = Math.sqrt(dx * dx + dz * dz);
        float pitch;

        if (invertRotation) {
            pitch = (float) (Math.atan2(dy, r) * 180 / Math.PI);
        } else {
            pitch = (float) (Math.atan2(-dy, r) * 180 / Math.PI);
        }

        this.pitch = pitch;
        return pitch;
    }
}