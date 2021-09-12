package com.demmodders.randomspawn.commands;

import com.datmodder.datsimplecommands.utils.PlayerManager;
import com.demmodders.datmoddingapi.delayedexecution.delayedevents.DelayedTeleportEvent;
import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.capability.RespawnProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;

public class DelayedSpawnEvent extends DelayedTeleportEvent {
    public DelayedSpawnEvent(EntityPlayerMP Player, int Delay) {
        // We don't need to store a destination, the util class handles it for us
        super(null, Player, Delay);
    }

    @Override
    public void execute(){
        // Forge Essentials
        if (Loader.isModLoaded("forgeessentials")) {
            // PlayerInfo.get(player.getUniqueID()).setLastTeleportOrigin(new WarpPoint(player.dimension, player.posX, player.posY, player.posZ, player.cameraPitch, player.rotationYaw));
        } else if (Loader.isModLoaded("datsimplecommands")) {
            PlayerManager.getInstance().updatePlayerBackLocation(player.getUniqueID(), new Location(player.dimension, player.posX, player.posY, player.posZ, player.cameraPitch, player.rotationYaw));
        }

        // Teleport the player to their spawn
        Util.teleportPlayer(player);

        // Set last teleport
        player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null).setLastTeleport(System.currentTimeMillis());
    }
}
