package com.demmodders.randomspawn.commands;

import com.demmodders.datmoddingapi.delayedexecution.delayedevents.DelayedTeleportEvent;
import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.randomspawn.Util;
import net.minecraft.entity.player.EntityPlayerMP;

public class DelayedSpawnEvent extends DelayedTeleportEvent {
    public DelayedSpawnEvent(EntityPlayerMP Player, int Delay) {
        super(null, Player, Delay);
    }

    @Override
    public void execute(){
        Util.teleportPlayer(player);
    }
}
