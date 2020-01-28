package com.demmodders.randomspawn.events;

import com.demmodders.factions.faction.FactionManager;
import com.demmodders.factions.util.FactionConfig;
import com.demmodders.randomspawn.RandomSpawn;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        NBTTagCompound test = e.player.getEntityData();
        if (test.getSize() == 0){
            test.setBoolean("joined", true);
        }
        FactionManager.getInstance()
    }
}
