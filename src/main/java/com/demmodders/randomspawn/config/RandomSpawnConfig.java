package com.demmodders.randomspawn.config;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RandomSpawn.MODID)
public class RandomSpawnConfig {
    @Config.Name("Spawn Dimension")
    @Config.Comment("The dimension that the player will spawn in (World is 0, nether is -1, end is 1, mod dimensions are also accepted)")
    public static int spawnDimension = 0;

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

    @Config.Name("Spawn Radius")
    @Config.Comment("The maximum distance from the origin that the player can spawn")
    @Config.RangeInt(min = 0)
    public static int spawnDistance = 1000;

    @Config.Name("Spawn Centre X")
    @Config.Comment("The x component of the centre of the are where players spawn")
    public static int spawnX = 0;

    @Config.Name("Spawn Centre Z")
    @Config.Comment("The z component of the centre of the are where players spawn")
    public static int spawnZ = 0;



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
