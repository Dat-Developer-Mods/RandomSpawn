package com.demmodders.randomspawn;

import com.demmodders.randomspawn.commands.CommandRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RandomSpawn.MODID, name = RandomSpawn.NAME,  version = RandomSpawn.VERSION, acceptableRemoteVersions  = "*", dependencies = "required-after:datmoddingapi@[1.2.1,)")
public class RandomSpawn
{
    public static final String MODID = "datrandomspawn";
    public static final String NAME = "Random Spawn";
    public static final String VERSION = "1.2.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register permissions
        CommandRegister.registerPermissionNodes();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent e){
        // register commands
        CommandRegister.registerCommands(e);
    }
}
