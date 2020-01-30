package com.demmodders.randomspawn.config;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraftforge.common.config.Config;

@Config(modid = RandomSpawn.MODID, name = RandomSpawn.MODID + "/Common")
public class RandomSpawnConfig {
    @Config.Name("Spawn Dimension")
    @Config.Comment("The dimension that the player will spawn in (World is 0, nether is -1, end is 1, mod dimensions also accepted)")
    public static int spawnDimension = 0;

    @Config.Name("Save Spawn")
    @Config.Comment("Should the player spawn at the same place every time the spawn")
    public static boolean saveSpawn = true;
}
