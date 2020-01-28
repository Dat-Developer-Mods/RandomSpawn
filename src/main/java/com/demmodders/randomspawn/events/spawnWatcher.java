package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        NBTTagCompound test = e.player.getEntityData();
        if (test.hasKey("player.PERSISTED_NBT_TAG")) {
            if (!test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean(RandomSpawn.MODID + ".joined")) {
                test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(RandomSpawn.MODID + ".joined", true);
                // TODO: Teleport
            }
        } else {
            test.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
            test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(RandomSpawn.MODID + ".joined", true);
            // TODO: Teleport
        }
    }
}
