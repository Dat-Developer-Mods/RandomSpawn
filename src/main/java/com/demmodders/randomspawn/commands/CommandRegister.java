package com.demmodders.randomspawn.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class CommandRegister {
    public static void registerPermissionNodes() {
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawn", DefaultPermissionLevel.ALL, "Enables the user to use /spawn");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnother", DefaultPermissionLevel.OP, "Enables the user to use /spawn on other players");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnreset", DefaultPermissionLevel.ALL, "Enables the user to use /spawnreset");
        PermissionAPI.registerNode("datrandomteleport.rspawn.spawnresetother", DefaultPermissionLevel.OP, "Enables the user to use /spawnreset on other players");
        PermissionAPI.registerNode("datrandomteleport.rspawn.admin", DefaultPermissionLevel.OP, "Enables the user to use admin commands to configure the mod");
    }

    public static void registerCommands(FMLServerStartingEvent e){
        e.registerServerCommand(new RandomSpawnCommand());
        e.registerServerCommand(new ResetSpawnCommand());
        e.registerServerCommand(new SetSpawnCommand());
        e.registerServerCommand(new SetPlayerSpawnCommand());
    }
}
