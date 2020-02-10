package com.demmodders.randomspawn.commands;

import com.demmodders.datmoddingapi.delayedexecution.delayedevents.DelayedTeleportEvent;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.structures.Player;
import com.forgeessentials.commons.selections.WarpPoint;
import com.forgeessentials.util.PlayerInfo;
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
            PlayerInfo.get(player.getUniqueID()).setLastTeleportOrigin(new WarpPoint(player.dimension, player.posX, player.posY, player.posZ, player.cameraPitch, player.rotationYaw));
        }

        // Teleport the player to their spawn
        Util.teleportPlayer(player, true);

        // Get the player
        Player thePlayer = Util.getPlayer(player.getUniqueID());
        if (thePlayer == null) thePlayer = Util.createPlayer();

        // Update their last teleport so we can check the cool down later

        thePlayer.lastTeleport = System.currentTimeMillis();

        Util.savePlayer(player.getUniqueID(), thePlayer);
    }
}
