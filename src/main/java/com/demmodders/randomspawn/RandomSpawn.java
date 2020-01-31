package com.demmodders.randomspawn;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = RandomSpawn.MODID, name = RandomSpawn.NAME,  version = RandomSpawn.VERSION, acceptableRemoteVersions  = "*")
public class RandomSpawn
{
    public static final String MODID = "datrandomteleport";
    public static final String NAME = "Random Teleport";
    public static final String VERSION = "0.0.1";
    public static final String MC_VERSION = "[1.12.2]";
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event){
        FMLCommonHandler.instance().getMinecraftServerInstance().getSpawnRadius()
    }
}
