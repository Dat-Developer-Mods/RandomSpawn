package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        // Check they're joining for the first time (so they don't randomly teleport when they join for the first time)
        if (Util.getPlayer(e.player.getUniqueID()) == null){
            Util.teleportPlayer((EntityPlayerMP) e.player, true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        if (!e.isEndConquered() && e.player.getBedLocation(e.player.dimension) == null) {
            Util.teleportPlayer((EntityPlayerMP) e.player ,false);
        }
    }
}
