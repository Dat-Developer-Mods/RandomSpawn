package com.demmodders.randomspawn.capability;

import net.minecraft.util.math.BlockPos;

public class Respawn implements IRespawn{
    private long lastTeleport;
    private BlockPos spawn;
    private int spawnDimension;

    @Override
    public long getLastTeleport() {
        return lastTeleport;
    }

    @Override
    public void setLastTeleport(long lastTeleport) {
        this.lastTeleport = lastTeleport;
    }

    @Override
    public BlockPos getSpawn() {
        return spawn;
    }

    @Override
    public void setSpawn(BlockPos spawn) {
        this.spawn = spawn;
    }

    @Override
    public int getSpawnDimension() {
        return spawnDimension;
    }

    @Override
    public void setSpawnDimension(int spawnDimension) {
        this.spawnDimension = spawnDimension;
    }

    @Override
    public void set(IRespawn respawn) {
        this.lastTeleport = respawn.getLastTeleport();
        this.spawn = new BlockPos(respawn.getSpawn());
        this.spawnDimension = respawn.getSpawnDimension();
    }
}
