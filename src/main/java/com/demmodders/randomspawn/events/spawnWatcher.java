package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    public static final Logger LOGGER = LogManager.getLogger(RandomSpawn.MODID);
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        NBTTagCompound test = e.player.getEntityData();
        if (test.hasKey("player.PERSISTED_NBT_TAG")) {
            if (!test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean(RandomSpawn.MODID + ".joined")) {
                test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(RandomSpawn.MODID + ".joined", true);
                LOGGER.info(e.player.posX);
                // TODO: Teleport
            }
        } else {
            test.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
            test.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(RandomSpawn.MODID + ".joined", true);
            // TODO: Teleport
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        e.player.setPositionAndUpdate(22f, 300f, 22f);
    }

    private void teleportPlayer(EntityPlayerMP player){

    }
}
