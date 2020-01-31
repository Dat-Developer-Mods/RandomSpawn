package com.demmodders.randomspawn.config;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraftforge.common.config.Config;

@Config(modid = RandomSpawn.MODID, name = RandomSpawn.MODID + "/Common")
public class RandomSpawnConfig {
    @Config.Name("Spawn Dimension")
    @Config.Comment("The dimension that the player will spawn in (World is 0, nether is -1, end is 1, mod dimensions also accepted)")
    public static int spawnDimension = 0;

    @Config.Name("Save Spawn")
    @Config.Comment("Should the player spawn at the same place every time they spawn")
    public static boolean saveSpawn = true;

    @Config.Name("/Spawn Delay")
    @Config.Comment("How long the player must wait before they can use /spawn again (0 for disabled)")
    @Config.RangeInt(min = 0)
    public static int spawnDelay = 0;

    @Config.Name("Spawn Radius")
    @Config.Comment("The maximum distance from the origin that the player can spawn")
    @Config.RangeInt(min = 0)
    public static int spawnDistance = 1000;
}
