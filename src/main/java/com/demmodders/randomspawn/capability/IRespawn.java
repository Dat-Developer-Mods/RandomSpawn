package com.demmodders.randomspawn.capability;

import net.minecraft.util.math.BlockPos;

public interface IRespawn {
    long getLastTeleport();

    void setLastTeleport(long lastTeleport);

    BlockPos getSpawn();

    void setSpawn(BlockPos spawn);

    int getSpawnDimension();

    void setSpawnDimension(int spawnDimension);

    void set(IRespawn oldRespawn);
}
