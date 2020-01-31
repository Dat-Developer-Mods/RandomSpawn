package com.demmodders.randomspawn.commands;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class CommandRegister {
    public static void registerPermissionNodes() {
        // Default
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawn", DefaultPermissionLevel.ALL, "Enables the user to use /spawn");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnother", DefaultPermissionLevel.ALL, "Enables the user to use /spawn on other players");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnreset", DefaultPermissionLevel.ALL, "Enables the user to use /spawnreset");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnresetother", DefaultPermissionLevel.ALL, "Enables the user to use /spawnreset on other players");
    }

    public static void registerCommands(FMLServerStartingEvent e){
        e.registerServerCommand(new RandomSpawnCommand());
        e.registerServerCommand(new ResetSpawnCommand());
    }
}
