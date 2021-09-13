package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.capability.IRespawn;
import com.demmodders.randomspawn.capability.RespawnProvider;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        // TODO: Handle default spawn config
        // Set their spawn if they've joined for the first time
        if (e.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null).getSpawn() == null) {
            if (RandomSpawnConfig.forceSpawnDimension) {
                e.player.setSpawnDimension(RandomSpawnConfig.defaultSpawnDimension);
            }
            Util.resetPlayerSpawn((EntityPlayerMP) e.player, RandomSpawnConfig.defaultSpawnDimension);

            Util.teleportPlayer((EntityPlayerMP) e.player);
        }
    }

    @SubscribeEvent
    public static void playerDie(final LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

        // TODO: Check this is correct
        if (player.forceSpawn) {
            if (!RandomSpawnConfig.saveSpawn) Util.resetPlayerSpawn(player, player.getSpawnDimension());
        } else {
            int dimension = Util.getValidSpawnDimension(player, player.dimension);
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
            BlockPos spawn = player.getBedSpawnLocation(world, player.bedLocation, player.forceSpawn);

            if (spawn == null) {
                if (RandomSpawnConfig.saveSpawn) {
                    IRespawn respawn = player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
                    player.setSpawnChunk(respawn.getSpawn(), true, dimension);
                } else {
                    Util.resetPlayerSpawn(player, dimension);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        if (!e.isEndConquered()) {
            IRespawn respawn = e.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);

            respawn.setLastTeleport(System.currentTimeMillis());
        }
    }
}
