package com.demmodders.randomspawn.structures;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class Player {
    public HashMap<Integer, BlockPos> spawn = new HashMap<>();
    transient public long lastTeleport = 0;

    // Empty constructor for GSON
    public Player(){

    }

    public Player(int Dim, BlockPos Spawn){
        spawn.put(Dim, Spawn);
    }

    public void setDimSpawn(int Dim, BlockPos Spawn){
        spawn.put(Dim, Spawn);
    }
}
