package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.capability.IRespawn;
import com.demmodders.randomspawn.capability.RespawnProvider;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        // Set their spawn if they've joined for the first time
        if (e.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null).getSpawn() == null) {
            if (RandomSpawnConfig.forceSpawnDimension) {
                e.player.setSpawnDimension(RandomSpawnConfig.defaultSpawnDimension);
            }
            Util.resetPlayerSpawn((EntityPlayerMP) e.player);
        }
//        if (Util.getPlayer(e.player.getUniqueID()) == null){
//            RandomSpawn.LOGGER.info("Dat Random Spawn has noticed that a player has joined for the first time");
//            Util.teleportPlayer((EntityPlayerMP) e.player, true);
//        } else {
//            RandomSpawn.LOGGER.info("Dat Random Spawn has noticed that a player has not joined for the first time");
//        }
    }

    @SubscribeEvent
    public static void playerDie(final LivingDeathEvent event) {
        if (!RandomSpawnConfig.saveSpawn) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            Util.resetPlayerSpawn(player);
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        // TODO: check if bedpos is not set, if it isn't restore from nbt
        if (!e.isEndConquered()) {
            IRespawn respawn = e.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);

            respawn.setLastTeleport(System.currentTimeMillis());
        }
    }
}
