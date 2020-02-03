package com.demmodders.randomspawn.structures;

import net.minecraft.util.math.BlockPos;

public class Player {
    public BlockPos spawn;
    public long lastTeleport = 0;

    // Empty constructor for gson
    public Player(){

    }

    public Player(BlockPos Spawn){
        spawn = Spawn;
    }
}
