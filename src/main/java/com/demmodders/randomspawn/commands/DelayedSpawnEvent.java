package com.demmodders.randomspawn.commands;

import com.demmodders.datmoddingapi.delayedexecution.delayedevents.DelayedTeleportEvent;
import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.structures.Player;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedSpawnEvent extends DelayedTeleportEvent {
    public DelayedSpawnEvent(EntityPlayerMP Player, int Delay) {
        // We don't need to store a destination, the util class handles it for us
        super(null, Player, Delay);
    }

    @Override
    public void execute(){
        // Teleport the player to their spawn
        Util.teleportPlayer(player);

        // Get the player
        Player thePlayer = Util.getPlayer(player.getUniqueID());
        if (thePlayer == null) thePlayer = Util.createPlayer();

        // Update their last teleport so we can check the cool down later
        thePlayer.lastTeleport = System.currentTimeMillis();
        Util.savePlayer(player.getUniqueID(), thePlayer);
    }
}
