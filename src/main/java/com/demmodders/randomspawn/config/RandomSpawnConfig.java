package com.demmodders.randomspawn.config;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RandomSpawn.MODID)
public class RandomSpawnConfig {
    @Config.Name("Force Spawn Dimension")
    @Config.Comment("Should the player always spawn in the same world")
    public static boolean forceSpawnDimension = false;

    @Config.Name("Default target Dimension")
    @Config.Comment("The dimension that the player will go to when they spawn in for the first time, if they respawn and Force Spawn Dimension is enabled, and when they use /spawn (World is 0, nether is -1, end is 1, mod dimensions are also accepted)\n(This nod is not nearly setup for spawning in the end or nether, this option has been added of mod dimensions, you will not spawn in a sensible location in either of those dimensions)")
    public static int defaultSpawnDimension = 0;

    @Config.Name("Save Spawn")
    @Config.Comment("Should the player spawn at the same place every time they go to spawn")
    public static boolean saveSpawn = true;

    @Config.Name("Spawn in water")
    @Config.Comment("Should the player be able spawn in water")
    public static boolean waterSpawn = true;

    @Config.Name("Safe spawn")
    @Config.Comment("Should the game check the player's spawn is safe (not in blocks or with a drop below it) and give them a new spawn if it is?")
    public static boolean safeSpawn = true;

    @Config.Name("/Spawn Delay")
    @Config.Comment("How long the player must wait to teleport to spawn (0 for disabled)")
    @Config.RangeInt(min = 0)
    public static int spawnDelay = 5;

    @Config.Name("/Spawn Cooldown")
    @Config.Comment("How long the player must wait before they can use /spawn again (0 for disabled)")
    @Config.RangeInt(min = 0)
    public static int spawnReDelay = 5;

    @Config.Name("Split Radius into X and Z")
    @Config.Comment("Uses different distances for X and Z")
    public static boolean splitRadius = false;

    @Config.Name("Spawn Radius X")
    @Config.Comment("The maximum distance from the origin that the player can spawn in the x direction")
    @Config.RangeInt(min = 0)
    public static int spawnDistanceX = 1000;

    @Config.Name("Spawn Radius Z")
    @Config.Comment("The maximum distance from the origin that the player can spawn in the Z direction (only used if Split Radius into X and Z is enabled)")
    @Config.RangeInt(min = 0)
    public static int spawnDistanceZ = 1000;

    @Config.Name("Spawn Centre X")
    @Config.Comment("The x component of the centre of the are where players spawn")
    public static int spawnX = 0;

    @Config.Name("Spawn Centre Z")
    @Config.Comment("The z component of the centre of the are where players spawn")
    public static int spawnZ = 0;

    @Config.Name("Accurate Y Position Generation")
    @Config.Comment("Should the mod use a slower but more accurate method of deciding where to place the player on the Y Axis")
    public static boolean accurateGeneration = false;

    @Config.Name("Max Generation Attempts")
    @Config.Comment("How many times should the mod try to generate a spawn position after failing before defaulting to the Spawn Centre (-1 for infinite)")
    @Config.RangeInt(min = -1)
    public static int generationRetries = 5;


    @Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void configChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RandomSpawn.MODID)) {
                ConfigManager.sync(RandomSpawn.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
